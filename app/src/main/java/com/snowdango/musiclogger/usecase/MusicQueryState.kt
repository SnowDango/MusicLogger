package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.repository.ontime.IsSaveState
import com.snowdango.musiclogger.repository.ontime.NowPlayData

class MusicQueryState() {

    suspend fun isPlaySongChanged(packageName: String, queueId: Long): Boolean {
        return NowPlayData.isMusicChange(packageName, queueId)
    }

    suspend fun isSaveStatePair(packageName: String, queueId: Long): IsSaveState{
        return NowPlayData.isSaveState(packageName, queueId)
    }

}