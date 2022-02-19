package com.snowdango.musiclogger.ui.album

import com.airbnb.epoxy.TypedEpoxyController
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import com.snowdango.musiclogger.view.item.albumView

class AlbumEpoxyController(private val artworkSize: Int) :
    TypedEpoxyController<ModelState<List<AlbumWithArt>>>() {

    override fun buildModels(data: ModelState<List<AlbumWithArt>>) {
        when (data) {
            is ModelState.Success -> {
                val albumList = data.data
                albumList.forEachIndexed { index, albumWithArt ->
                    albumView {
                        id(index)
                        artworkSize(this@AlbumEpoxyController.artworkSize)
                        albumWithArt(albumWithArt)
                    }
                }
            }
            else -> {}
        }
    }

}