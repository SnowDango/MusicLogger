package com.snowdango.musiclogger.extention

import android.content.SharedPreferences
import com.snowdango.musiclogger.domain.session.MusicApp

const val USE_CUTOUT_AREA = "cutout"

const val APPLE_SESSION = "session-apple"
const val ARTWORK_QUERY = "artwork-query"

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

var SharedPreferences.useCutoutArea: Boolean
    get() = getBoolean(USE_CUTOUT_AREA, false)
    set(value) = edit().putBoolean(USE_CUTOUT_AREA, value).apply()


enum class ArtQueryRegisterState(val state: Int){
    REGISTER(0),
    UNREGISTER(1)
}


fun SharedPreferences.registerArtQuery(album: String, artist: String){
    edit().putInt("$ARTWORK_QUERY-$album-$artist", ArtQueryRegisterState.REGISTER.state).apply()
}

fun SharedPreferences.removeArtQuery(album: String, artist: String){
    edit().remove("$ARTWORK_QUERY-$album-$artist").apply()
}

fun SharedPreferences.getArtQuery(album: String, artist: String): Int{
    return getInt("$ARTWORK_QUERY-$album-$artist", ArtQueryRegisterState.UNREGISTER.state)
}


