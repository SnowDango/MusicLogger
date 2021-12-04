package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import org.koin.core.component.KoinComponent

class LoadMusicHistory(private val musicDataBase: MusicDataBase) : KoinComponent {

    suspend fun loadMusicHistory(offset: Int = 0): List<MusicMetaWithArt> {
        return musicDataBase.musicMetaWithArtDao().getCompleteMusicMetaLimit100(offset)
    }

}