package com.snowdango.musiclogger.model

import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.usecase.LoadMusicHistory
import com.snowdango.musiclogger.usecase.MoreLoadMusicHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicHistoryModel : KoinComponent {

    private val loadMusicHistory by inject<LoadMusicHistory>()
    private val moreLoadMusicHistory by inject<MoreLoadMusicHistory>()

    suspend fun getMusicHistory(): ModelState<List<MusicMetaWithArt>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = loadMusicHistory.loadMusicHistory()
                ModelState.Success(result)
            }
        } catch (e: Exception) {
            ModelState.Failed(e)
        }
    }

    suspend fun getMoreFetchHistory(modelState: ModelState<List<MusicMetaWithArt>>): ModelState<List<MusicMetaWithArt>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = moreLoadMusicHistory.moreLoadMusicHistory(modelState)
                ModelState.Success(result)
            }
        } catch (e: Exception) {
            ModelState.Failed(e)
        }
    }
}