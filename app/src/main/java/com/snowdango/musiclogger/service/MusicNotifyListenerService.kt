package com.snowdango.musiclogger.service

import android.app.Notification
import android.content.Intent
import android.media.session.MediaController
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.*
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.usecase.MusicSessionState.isPlaySongChanged
import com.snowdango.musiclogger.usecase.SaveMetaData.saveMusic
import com.snowdango.musiclogger.usecase.SessionData.getSongMetadata
import android.app.NotificationManager

import androidx.core.app.NotificationCompat

import android.app.NotificationChannel
import android.content.Context
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.SERVICE_NOTIFICATION_ID
import com.snowdango.musiclogger.usecase.SaveMetaData.saveArtwork


class MusicNotifyListenerService: NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        startForeground()
        return super.onBind(intent)
    }

    private fun startForeground() {
        val channelName = "MusicLogger"
        val channel = NotificationChannel(
            SERVICE_NOTIFICATION_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        ).also { it.lockscreenVisibility = Notification.VISIBILITY_PRIVATE }
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).also {
            it.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, SERVICE_NOTIFICATION_ID)
        val notification: Notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_android)
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setWhen(System.currentTimeMillis())
            .setOngoing(true)
            .build()
        startForeground(2, notification)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notify = sbn?.notification
        notify?.let { _ ->
            if(notify.category == Notification.CATEGORY_TRANSPORT){
                getMediaMetadata(sbn.packageName)?.let { mediaController ->
                    val dataBase = MusicDataBase.getDatabase(applicationContext)
                    getSongMetadata(mediaController)?.let { musicMeta ->
                        mediaController.queue?.let {
                            if(isPlaySongChanged(applicationContext, mediaController.packageName, it.first().queueId)){
                                saveMusic(dataBase, musicMeta, applicationContext)
                            }else{
                                saveArtwork(dataBase, musicMeta, applicationContext)
                            }
                        }
                    }
                }
            }
        }
        super.onNotificationPosted(sbn)
    }
}