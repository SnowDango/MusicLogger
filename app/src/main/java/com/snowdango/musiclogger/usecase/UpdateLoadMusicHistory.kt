package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.MusicDataBase
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import org.koin.core.component.KoinComponent

class UpdateLoadMusicHistory(private val musicDataBase: MusicDataBase) : KoinComponent {

    suspend fun updateLoadMusicHistory(modelState: ModelState<List<MusicMetaWithArt>>): List<MusicMetaWithArt> {
        return if (modelState is ModelState.Success) {
            val size = modelState.data.size
            musicDataBase.musicMetaWithArtDao().getUpdateMusicMata(size + 1)
        } else {
            musicDataBase.musicMetaWithArtDao().getCompleteMusicMetaLimit100(0)
        }
    }

}