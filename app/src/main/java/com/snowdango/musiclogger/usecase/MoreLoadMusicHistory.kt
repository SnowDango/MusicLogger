package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import org.koin.core.component.KoinComponent

class MoreLoadMusicHistory(private val loadMusicHistory: LoadMusicHistory) : KoinComponent {

    suspend fun moreLoadMusicHistory(modelState: ModelState<List<MusicMetaWithArt>>): List<MusicMetaWithArt> {
        return when (modelState) {
            is ModelState.Success -> {
                val offset = modelState.data.size + 1
                val result = loadMusicHistory.loadMusicHistory(offset)
                modelState.data + result
            }
            else -> {
                loadMusicHistory.loadMusicHistory()
            }

        }

    }
}