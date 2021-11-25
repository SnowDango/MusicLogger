package com.snowdango.musiclogger.extention

import android.content.SharedPreferences
import com.snowdango.musiclogger.domain.session.MusicApp

const val APPLE_SESSION = "session-apple"

fun SharedPreferences.getSession(packageName: String): Long{
    return when(packageName){
        MusicApp.AppleMusic.pkg -> appleSession
        else -> 0
    }
}

fun SharedPreferences.setSession(packageName: String, sessionId: Long){
    when(packageName){
        MusicApp.AppleMusic.pkg -> appleSession = sessionId
        else -> {}
    }
}

var SharedPreferences.appleSession: Long
    get() = getLong(APPLE_SESSION, 0)
    set(value) = edit().putLong(APPLE_SESSION, value).apply()