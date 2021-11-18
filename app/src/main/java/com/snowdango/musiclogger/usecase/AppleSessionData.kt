package com.snowdango.musiclogger.usecase

import android.media.MediaMetadata
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.*

object AppleSessionData {

    fun getMeta(mediaMetadata: MediaMetadata): MusicMeta{
        return MusicMeta(
            title = mediaMetadata.title(),
            artist = mediaMetadata.artist(),
            album = mediaMetadata.album(),
            mediaId = mediaMetadata.mediaId(),
            artwork = mediaMetadata.artwork()
        )
    }
}