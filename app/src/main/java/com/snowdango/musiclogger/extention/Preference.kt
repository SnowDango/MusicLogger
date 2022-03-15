package com.snowdango.musiclogger.extention

import android.content.SharedPreferences

const val USE_CUTOUT_AREA = "cutout"

const val QUEUE_ID = "queue-id"
const val COMP_DATA = "complete"
const val QUEUE_ARTWORK = "queue-artwork"
const val LAST_ID = "last-id"

const val SPOTIFY_TOKEN = "spotify-token"
const val SPOTIFY_TOKEN_LAST_UPDATE = "spotify-token-last-update"

// last session data
fun SharedPreferences.getQuery(
    appString: String
): QueryPreferences {
    return QueryPreferences(
        getLong("$QUEUE_ID-$appString", 0),
        getBoolean("$COMP_DATA-$appString", true),
        getBoolean("$QUEUE_ARTWORK-$appString", false)
    )
}

fun SharedPreferences.setQuery(
    appString: String, query: QueryPreferences
) {
    edit().putLong("$QUEUE_ID-$appString", query.queueId).apply()
    edit().putBoolean("$COMP_DATA-$appString", query.isComplete).apply()
    edit().putBoolean("$QUEUE_ARTWORK-$appString", query.isSaveArtwork).apply()
    saveLastId(-1L, appString)
}

fun SharedPreferences.saveArtwork(
    appString: String
) {
    edit().putBoolean("$QUEUE_ARTWORK-$appString", true).apply()
}

fun SharedPreferences.dataComplete(
    appString: String
) {
    edit().putBoolean("$COMP_DATA-$appString", true).apply()
}

fun SharedPreferences.saveLastId(
    id: Long, appString: String
) {
    edit().putLong("$LAST_ID-$appString", id).apply()
}

fun SharedPreferences.getLastId(
    appString: String
): Long {
    return getLong("$LAST_ID-$appString", -1L)
}

var SharedPreferences.spotifyToken: String
    set(value) = edit().putString(SPOTIFY_TOKEN, value).apply()
    get() = getString(SPOTIFY_TOKEN, "").toString()

var SharedPreferences.spotifyTokenLastUpdate: Long
    set(value) = edit().putLong(SPOTIFY_TOKEN_LAST_UPDATE, value).apply()
    get() = getLong(SPOTIFY_TOKEN_LAST_UPDATE, 0)

class QueryPreferences(
    val queueId: Long,
    val isComplete: Boolean,
    val isSaveArtwork: Boolean
)


