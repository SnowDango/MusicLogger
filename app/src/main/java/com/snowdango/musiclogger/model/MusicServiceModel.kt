package com.snowdango.musiclogger.model

import android.app.Notification
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSession.QueueItem
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.getMediaMetadata
import com.snowdango.musiclogger.usecase.artwork.SaveArtwork
import com.snowdango.musiclogger.usecase.history.SaveMusicHistory
import com.snowdango.musiclogger.usecase.query.MusicQueryState
import com.snowdango.musiclogger.usecase.session.SessionMetaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber


class MusicServiceModel(private val context: Context) : KoinComponent {

    private val musicSessionState by inject<MusicQueryState>()
    private val saveMusic by inject<SaveMusicHistory>()
    private val saveArtwork by inject<SaveArtwork>()
    private val sessionData by inject<SessionMetaData>()

    private val mutex: Mutex = Mutex()

    fun notifyListen(notification: Notification, packageName: String) =
        CoroutineScope(Dispatchers.Default).launch {
            if (notification.category != Notification.CATEGORY_TRANSPORT) return@launch
            val pairData = isNeedInfoNotNull(context.getMediaMetadata(packageName))

            pairData?.let {
                val musicMeta = pairData.first
                val queueItem = pairData.second
                saveHistory(musicMeta, queueItem)
            }
        }

    private suspend fun saveHistory(musicMeta: MusicMeta, queueItem: QueueItem) {
        mutex.withLock {
            when (musicSessionState.checkChangePlaying(queueItem.queueId, musicMeta.app)) {
                MusicQueryState.QueryState.COMPLETE -> {
                    Timber.d("save music complete")
                }
                MusicQueryState.QueryState.CHANGED -> {
                    Timber.d("save music and artwork")
                    saveMusic.saveMusic(musicMeta)
                    musicMeta.artwork?.let { saveArtwork.saveArtwork(musicMeta) }
                }
                MusicQueryState.QueryState.NOT_ARTWORK -> {
                    Timber.d("save artwork")
                    musicMeta.artwork?.let { saveArtwork.saveArtwork(musicMeta) }
                }
                MusicQueryState.QueryState.NOT_COMP -> {
                    Timber.d("update music data")
                    saveMusic.updateMusicData(musicMeta)
                }
                MusicQueryState.QueryState.NOT_ARTWORK_AND_NOT_COMP -> {
                    Timber.d("update and save artwork")
                    saveMusic.updateMusicData(musicMeta)
                    musicMeta.artwork?.let { saveArtwork.saveArtwork(musicMeta) }
                }
            }
        }
    }

    private fun isNeedInfoNotNull(
        mediaController: MediaController?
    ): Pair<MusicMeta, MediaSession.QueueItem>? {
        mediaController?.let { controller ->
            sessionData.getSongMetaData(controller)?.let { metadata ->
                mediaController.queue?.let {
                    return Pair(metadata, it.first())
                }
            }
        }
        return null
    }
}