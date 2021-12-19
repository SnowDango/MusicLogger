package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.repository.ontime.IsSaveState
import com.snowdango.musiclogger.repository.ontime.NowPlayData

class MusicQueryState() {

    suspend fun isPlaySongChanged(packageName: String, queueId: Long, isComp: Boolean): Pair<Boolean, Boolean> {
        return NowPlayData.isMusicChange(packageName, queueId, isComp)
    }

    suspend fun isSaveStatePair(packageName: String, queueId: Long, isComp: Boolean): IsSaveState {
        return NowPlayData.isSaveState(packageName, queueId, isComp)
    }

}