package com.snowdango.musiclogger.model

import android.app.Notification
import android.content.Context
import android.util.Log
import com.snowdango.musiclogger.connection.ViewObject
import com.snowdango.musiclogger.extention.getMediaMetadata
import com.snowdango.musiclogger.usecase.MusicSessionState
import com.snowdango.musiclogger.usecase.SaveArtwork
import com.snowdango.musiclogger.usecase.SaveMusicHistory
import com.snowdango.musiclogger.usecase.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MusicServiceModel(private val context: Context) : KoinComponent {

    private val musicSessionState by inject<MusicSessionState>()
    private val saveMusic by inject<SaveMusicHistory>()
    private val saveArtwork by inject<SaveArtwork>()
    private val sessionData by inject<SessionData>()

    fun notifyListen(notification: Notification, packageName: String) =
        CoroutineScope(Dispatchers.Default).launch {
            if (notification.category == Notification.CATEGORY_TRANSPORT) {
                context.getMediaMetadata(packageName)?.let { mediaController ->
                    sessionData.getSongMetadata(mediaController)?.let { musicMeta ->
                        mediaController.queue?.let {
                            withContext(Dispatchers.IO) {
                                if (musicSessionState.isPlaySongChanged(
                                        mediaController.packageName,
                                        it.first().queueId
                                    )
                                ) {
                                    Log.d("mate-data ", musicMeta.toString())
                                    ViewObject.musicMeta.postValue(musicMeta)
                                    saveArtwork.saveArtwork(musicMeta)
                                    saveMusic.saveMusic(musicMeta)
                                } else {
                                    saveArtwork.saveArtwork(musicMeta)
                                }
                            }
                        }
                    }
                }
            }
        }

}