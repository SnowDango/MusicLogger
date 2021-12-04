package com.snowdango.musiclogger.service

import android.app.Notification
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.content.Context
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.SERVICE_NOTIFICATION_ID
import com.snowdango.musiclogger.model.MusicServiceModel
import org.koin.android.ext.android.inject


class MusicNotifyListenerService: NotificationListenerService() {

    private val model by inject<MusicServiceModel>()

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
        notify?.let { _ -> model.notifyListen(notify, sbn.packageName) }
        super.onNotificationPosted(sbn)
    }
}