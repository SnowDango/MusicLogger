package com.snowdango.musiclogger.domain.session

import android.graphics.Bitmap

data class AppleMusicMeta(
    val title: String?,
    val artist: String?,
    val album: String?,
    val mediaId: String?,
    val image: Bitmap?
)


