package com.snowdango.musiclogger.service

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

class MusicNotifyListenerService: NotificationListenerService() {

    override fun onBind(intent: Intent?): IBinder? {
        setForegroundNotify(intent)
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notify = sbn?.notification
        notify?.let { _ ->
            if(notify.category == Notification.CATEGORY_TRANSPORT){
                val metadata: MediaMetadata? = fetchMetadata(sbn.packageName)
                metadata?.let {
                    Log.d("notificationTitle", metadata.getString(MediaMetadata.METADATA_KEY_TITLE))
                    ViewObject.art.postValue(metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART))
                }
            }
        }
        super.onNotificationPosted(sbn)
    }

    private fun setForegroundNotify(intent: Intent?){
        val context = applicationContext
        val channelId = "music_logger"
        val title = "Musicを記録しています。"
        val requestCode = 1
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)

        // 通知作成
        val notificationId = 1
        val notification = Notification.Builder(context, channelId)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_android)
            .setContentText("Musicの記録中")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build()

        // foreground service実行
        startForeground(notificationId, notification)
    }

    private fun fetchMetadata(packageName: String): MediaMetadata? =
        getSystemService(MediaSessionManager::class.java).let {
            val componentName = ComponentName(this, MusicNotifyListenerService::class.java)

            return@let it.getActiveSessions(componentName).firstOrNull { it.packageName == packageName }?.metadata
        }
}