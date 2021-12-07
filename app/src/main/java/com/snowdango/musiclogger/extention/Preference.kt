package com.snowdango.musiclogger.extention

import android.content.SharedPreferences

const val USE_CUTOUT_AREA = "cutout"

const val QUEUE_ID = "queue-id"
const val QUEUE_ARTWORK = "queue-artwork"

fun SharedPreferences.getQueueData(packageName: String): Pair<Long, Boolean>{
    return Pair(
        getLong("$QUEUE_ID-$packageName", 0),
        getBoolean("$QUEUE_ARTWORK-$packageName", false)
    )
}

fun SharedPreferences.setQueueData(packageName: String, queueId: Long, isArtworkSave: Boolean){
    edit().putLong("$QUEUE_ID-$packageName", queueId).apply()
    edit().putBoolean("$QUEUE_ARTWORK-$packageName", isArtworkSave).apply()
}

var SharedPreferences.useCutoutArea: Boolean
    get() = getBoolean(USE_CUTOUT_AREA, false)
    set(value) = edit().putBoolean(USE_CUTOUT_AREA, value).apply()



