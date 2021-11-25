package com.snowdango.musiclogger.usecase

import android.content.Context
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveMusic(private val musicDataBase: MusicDataBase, private val saveArtwork: SaveArtwork) {

    fun saveMusic(musicMeta: MusicMeta) = CoroutineScope(Dispatchers.IO).launch {
        saveArtwork.saveArtwork(musicMeta)
        musicDataBase.musicMetaDao().insert(
            MusicMetadata(
                title = musicMeta.title,
                album = musicMeta.album,
                artist = musicMeta.artist,
                albumArtist = musicMeta.albumArtist,
                listeningUnix = DateTime.nowUnixLong(),
                mediaId = musicMeta.mediaId,
                app = musicMeta.app
            )
        )
    }
}