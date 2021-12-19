package com.snowdango.musiclogger.extention

import android.content.SharedPreferences

const val USE_CUTOUT_AREA = "cutout"

const val QUEUE_ID = "queue-id"
const val COMP_DATA = "complete"
const val QUEUE_ARTWORK = "queue-artwork"
const val LAST_ID = "last-id"

fun SharedPreferences.getQueueData(packageName: String): QueryPreferences {
    return QueryPreferences(
        getLong("$QUEUE_ID-$packageName", 0),
        getBoolean("$COMP_DATA-$packageName", true),
        getLong("$LAST_ID-$packageName", 0),
        getBoolean("$QUEUE_ARTWORK-$packageName", false)
    )
}

fun SharedPreferences.setQueueData(
    packageName: String,
    queueId: Long,
    isComplete: Boolean,
    lastId: Long,
    isArtworkSave: Boolean
) {
    edit().putLong("$QUEUE_ID-$packageName", queueId).apply()
    edit().putBoolean("$COMP_DATA-$packageName", isComplete).apply()
    edit().putLong("$LAST_ID-$packageName", lastId).apply()
    edit().putBoolean("$QUEUE_ARTWORK-$packageName", isArtworkSave).apply()
}

var SharedPreferences.useCutoutArea: Boolean
    get() = getBoolean(USE_CUTOUT_AREA, false)
    set(value) = edit().putBoolean(USE_CUTOUT_AREA, value).apply()


data class QueryPreferences(
    val queueId: Long,
    val isComplete: Boolean,
    val lastId: Long,
    val queueArtwork: Boolean
)


