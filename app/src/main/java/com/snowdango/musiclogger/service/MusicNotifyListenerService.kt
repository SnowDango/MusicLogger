package com.snowdango.musiclogger.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.connection.ViewObject
import com.snowdango.musiclogger.extention.*

class MusicNotifyListenerService: NotificationListenerService() {

    val channelId = "music_logger"

    override fun onBind(intent: Intent?): IBinder? {
        setForegroundNotify(intent)
        return super.onBind(intent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setForegroundNotify(intent: Intent?){
        val requestCode = 1
        val pendingIntent = PendingIntent.getActivity(applicationContext, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationId = 1
        val notification = Notification.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_android)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build()
        startForeground(notificationId, notification)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notify = sbn?.notification
        notify?.let { _ ->
            if(notify.category == Notification.CATEGORY_TRANSPORT){
                val metadata: MediaMetadata? = fetchMetadata(sbn.packageName)
                metadata?.let {
                    Log.d("notificationTitle", metadata.title().toString())
                    Log.d("notificationArtist", metadata.artist().toString())
                    Log.d("notificationAlbum", metadata.album().toString())
                    Log.d("notificationMediaId", metadata.mediaId().toString())
                    ViewObject.art.postValue(metadata.artwork())
                }
            }
        }
        super.onNotificationPosted(sbn)
    }

    private fun fetchMetadata(packageName: String): MediaMetadata? =
        getSystemService(MediaSessionManager::class.java).let { sessionManager ->
            val componentName = ComponentName(this, MusicNotifyListenerService::class.java)
            return@let sessionManager.getActiveSessions(componentName).firstOrNull {
                it.packageName == packageName
            }?.metadata
        }
}