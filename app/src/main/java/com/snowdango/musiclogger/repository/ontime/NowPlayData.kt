package com.snowdango.musiclogger.repository.ontime

import androidx.lifecycle.MutableLiveData
import com.snowdango.musiclogger.domain.session.MusicMeta

object NowPlayData {

    val musicMeta = MutableLiveData<MusicMeta>()
}
