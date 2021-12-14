package com.snowdango.musiclogger.usecase

import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import org.koin.core.component.KoinComponent

class MoreLoadAlbum(private val loadAlbum: LoadAlbum) : KoinComponent {

    suspend fun moreLoadAlbum(modelState: ModelState<List<AlbumWithArt>>): List<AlbumWithArt> {
        return when (modelState) {
            is ModelState.Success -> {
                val offset = modelState.data.size + 1
                val result = loadAlbum.loadAlbum(offset)
                modelState.data + result
            }
            else -> loadAlbum.loadAlbum()
        }
    }
}