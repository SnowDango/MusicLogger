package com.snowdango.musiclogger.model.service

import android.app.Notification
import android.content.Context
import com.snowdango.musiclogger.extention.getMediaMetadata
import com.snowdango.musiclogger.usecase.MusicSessionState
import com.snowdango.musiclogger.usecase.SaveArtwork
import com.snowdango.musiclogger.usecase.SaveMusic
import com.snowdango.musiclogger.usecase.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MusicServiceModel(
    private val context: Context,
    private val musicSessionState: MusicSessionState,
    private val saveMusic: SaveMusic,
    private val saveArtwork: SaveArtwork,
    private val sessionData: SessionData
) {

    fun notifyListen(notification: Notification, packageName: String) {
            if(notification.category == Notification.CATEGORY_TRANSPORT){
            context.getMediaMetadata(packageName)?.let { mediaController ->
                sessionData.getSongMetadata(mediaController)?.let { musicMeta ->
                    mediaController.queue?.let {
                        if(musicSessionState.isPlaySongChanged(mediaController.packageName, it.first().queueId)){
                            saveMusic.saveMusic(musicMeta)
                        }else{
                            saveArtwork.saveArtwork(musicMeta)
                        }
                    }
                }
            }
        }
    }

}