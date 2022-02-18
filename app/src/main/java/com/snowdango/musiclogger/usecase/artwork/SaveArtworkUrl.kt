package com.snowdango.musiclogger.usecase.artwork

import com.snowdango.musiclogger.repository.db.MusicDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveArtworkUrl(private val musicDataBase: MusicDataBase) {

    suspend fun saveArtworkUrl(url: String, id: Long) = withContext(Dispatchers.IO) {
        musicDataBase.artworkDao().updateUrl(id, url)
    }

}