package com.snowdango.musiclogger.extention

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSessionManager
import com.snowdango.musiclogger.service.MusicNotifyListenerService

fun Context.getMediaMetadata(packageName: String): MediaController? =
    getSystemService(MediaSessionManager::class.java).let { sessionManager ->
        val componentName = ComponentName(this, MusicNotifyListenerService::class.java)
        return@let sessionManager.getActiveSessions(componentName).firstOrNull {
            it.packageName == packageName
        }
    }