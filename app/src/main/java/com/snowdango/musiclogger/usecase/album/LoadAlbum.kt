package com.snowdango.musiclogger.usecase.album

import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import org.koin.core.component.KoinComponent

class LoadAlbum(private val musicDataBase: MusicDataBase) : KoinComponent {

    suspend fun loadAlbum(offset: Int = 0): List<AlbumWithArt> {
        return musicDataBase.albumWithArtDao().getAlbumWithArtLimit100(offset)
    }
}