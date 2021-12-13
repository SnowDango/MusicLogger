package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.repository.db.MusicDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class UpdateMusicData(private val musicDataBase: MusicDataBase) : KoinComponent {

    suspend fun updateMusicData() = withContext(Dispatchers.IO) {
        
    }

}