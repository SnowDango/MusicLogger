package com.snowdango.musiclogger.model

import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import com.snowdango.musiclogger.usecase.album.LoadAlbum
import com.snowdango.musiclogger.usecase.album.MoreLoadAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryAlbumModel : KoinComponent {

    private val loadAlbum by inject<LoadAlbum>()
    private val moreLoadAlbum by inject<MoreLoadAlbum>()

    suspend fun getAlbum(): ModelState<List<AlbumWithArt>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = loadAlbum.loadAlbum()
                ModelState.Success(result)
            }
        } catch (e: Exception) {
            ModelState.Failed(e)
        }
    }

    suspend fun getMoreAlbum(modelState: ModelState<List<AlbumWithArt>>): ModelState<List<AlbumWithArt>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = moreLoadAlbum.moreLoadAlbum(modelState)
                ModelState.Success(result)
            }
        } catch (e: Exception) {
            ModelState.Failed(e)
        }
    }

}