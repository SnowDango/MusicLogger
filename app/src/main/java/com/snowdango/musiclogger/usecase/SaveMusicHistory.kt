package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.snowdango.musiclogger.repository.ontime.NowPlayData
import com.soywiz.klock.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMusicHistory(
    private val musicDataBase: MusicDataBase,
) {

    suspend fun saveMusic(musicMeta: MusicMeta, queueId: Long, packageName: String) = withContext(Dispatchers.IO) {
        val id = musicDataBase.musicMetaDao().insert(
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
        withContext(Dispatchers.Default) {
            NowPlayData.setLastId(packageName, id, queueId)
        }
    }
}