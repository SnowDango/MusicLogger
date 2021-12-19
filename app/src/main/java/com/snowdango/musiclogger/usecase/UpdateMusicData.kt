package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetadata
import com.snowdango.musiclogger.repository.ontime.NowPlayData
import com.soywiz.klock.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class UpdateMusicData(private val musicDataBase: MusicDataBase) : KoinComponent {

    suspend fun updateMusicData(musicMeta: MusicMeta, packageName: String, queueId: Long) =
        withContext(Dispatchers.IO) {
            val updateId = NowPlayData.getLastId(packageName, queueId)
            if (updateId != -1L) {
                musicDataBase.musicMetaDao().update(
                    MusicMetadata(
                        id = updateId,
                        title = musicMeta.title,
                        album = musicMeta.album,
                        artist = musicMeta.artist,
                        albumArtist = musicMeta.albumArtist,
                        app = musicMeta.app,
                        mediaId = musicMeta.mediaId,
                        listeningUnix = DateTime.nowUnixLong()
                    )
                )
            }
        }
}