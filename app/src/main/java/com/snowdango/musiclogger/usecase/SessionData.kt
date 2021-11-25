package com.snowdango.musiclogger.usecase

import android.media.MediaMetadata
import android.media.session.MediaController
import com.snowdango.musiclogger.domain.session.MusicApp
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.extention.*

class SessionData() {

    fun getSongMetadata(mediaController: MediaController): MusicMeta? {
        val packageName = mediaController.packageName
        val mediaMetadata = mediaController.metadata
        return when (packageName) {
            MusicApp.AppleMusic.pkg -> getAppleMeta(mediaMetadata)
            else -> null
        }
    }

    private fun getAppleMeta(mediaMetadata: MediaMetadata?): MusicMeta? {

        if (mediaMetadata == null) return null

        return MusicMeta(
            title = mediaMetadata.title(),
            artist = mediaMetadata.artist(),
            albumArtist = mediaMetadata.albumArtist(),
            album = mediaMetadata.album(),
            mediaId = mediaMetadata.mediaId(),
            artwork = mediaMetadata.artwork(),
            app = MusicApp.AppleMusic.string
        )
    }
}