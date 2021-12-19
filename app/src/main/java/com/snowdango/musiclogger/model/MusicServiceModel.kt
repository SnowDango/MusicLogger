package com.snowdango.musiclogger.model

import android.app.Notification
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSession
import android.util.Log
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.getMediaMetadata
import com.snowdango.musiclogger.repository.ontime.NowPlayData
import com.snowdango.musiclogger.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MusicServiceModel(private val context: Context) : KoinComponent {

    private val musicSessionState by inject<MusicQueryState>()
    private val saveMusic by inject<SaveMusicHistory>()
    private val updateMusic by inject<UpdateMusicData>()
    private val saveArtwork by inject<SaveArtwork>()
    private val sessionData by inject<SessionData>()

    fun notifyListen(notification: Notification, packageName: String) =
        CoroutineScope(Dispatchers.Default).launch {

            if (notification.category != Notification.CATEGORY_TRANSPORT) return@launch

            val pairData = isNeedInfoNotNull(context.getMediaMetadata(packageName))

            pairData?.let {
                val musicMeta = pairData.first
                val queueItem = pairData.second
                val isComp = isCompleteData(musicMeta)
                if (musicMeta.artwork == null) {
                    val isQuery = musicSessionState.isPlaySongChanged(packageName, queueItem.queueId, isComp)
                    musicSave(isQuery.first, isQuery.second, queueItem.queueId, musicMeta, packageName)
                } else {
                    val saveState = musicSessionState.isSaveStatePair(packageName, queueItem.queueId, isComp)
                    musicSave(saveState.isChange, saveState.isComplete, queueItem.queueId, musicMeta, packageName)
                    artworkSave(saveState.isSaveArtwork, queueItem.queueId, musicMeta)
                }
            }
        }

    private suspend fun musicSave(
        isChange: Boolean,
        isComp: Boolean,
        queueId: Long,
        musicMeta: MusicMeta,
        packageName: String
    ) {
        if (isChange) {
            Log.d("register", queueId.toString())
            NowPlayData.musicMeta.postValue(musicMeta)
            withContext(Dispatchers.IO) {
                saveMusic.saveMusic(musicMeta, queueId, packageName)
            }
        } else if (!isComp) {
            Log.d("complete", "not complete")
            updateMusic.updateMusicData(musicMeta, packageName, queueId)
        } else {
            Log.d("register", "is registed")
        }
    }

    private suspend fun artworkSave(isSaveState: Boolean, queueId: Long, musicMeta: MusicMeta) {
        if (!isSaveState) {
            Log.d("artwork-register", queueId.toString())
            NowPlayData.musicMeta.postValue(musicMeta)
            withContext(Dispatchers.IO) {
                saveArtwork.saveArtwork(musicMeta)
            }
        } else {
            Log.d("artwork-register", "is registed")
        }
    }

    private fun isNeedInfoNotNull(
        mediaController: MediaController?
    ): Pair<MusicMeta, MediaSession.QueueItem>? {
        mediaController?.let { controller ->
            sessionData.getSongMetadata(controller)?.let { metadata ->
                mediaController.queue?.let {
                    return Pair(metadata, it.first())
                }
            }
        }
        return null
    }

    private fun isCompleteData(musicMeta: MusicMeta): Boolean {
        return musicMeta.mediaId != null &&
                musicMeta.album != null &&
                musicMeta.albumArtist != null &&
                musicMeta.artist != null
    }
}