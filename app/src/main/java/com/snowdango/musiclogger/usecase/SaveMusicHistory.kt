package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.soywiz.klock.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMusicHistory(
    private val musicDataBase: MusicDataBase,
    private val saveArtwork: SaveArtwork
) {

    suspend fun saveMusic(musicMeta: MusicMeta) = withContext(Dispatchers.IO) {
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