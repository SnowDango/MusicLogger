package com.snowdango.musiclogger.usecase

import android.media.MediaMetadata
import com.snowdango.musiclogger.domain.session.AppleMusicMeta
import com.snowdango.musiclogger.repository.musicapp.*

object AppleSessionData {

    fun getMeta(mediaMetadata: MediaMetadata): AppleMusicMeta{
        return AppleMusicMeta(
            title = mediaMetadata.title(),
            artist = mediaMetadata.artist(),
            album = mediaMetadata.album(),
            mediaId = mediaMetadata.mediaId(),
            image = mediaMetadata.artwork()
        )
    }
}