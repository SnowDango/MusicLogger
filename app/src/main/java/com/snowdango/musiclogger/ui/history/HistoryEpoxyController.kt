package com.snowdango.musiclogger.ui.history

import com.airbnb.epoxy.TypedEpoxyController
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.item.historyView

class HistoryEpoxyController(private val artworkSize: Int) :
    TypedEpoxyController<ModelState<List<MusicMetaWithArt>>>() {

    override fun buildModels(data: ModelState<List<MusicMetaWithArt>>) {
        when (data) {
            is ModelState.Success -> {
                val historyList = data.data
                historyList.forEachIndexed { index, musicMetaWithArt ->
                    historyView {
                        id(index)
                        artworkSize(this@HistoryEpoxyController.artworkSize)
                        musicMetaWithArt(musicMetaWithArt)
                    }
                }
            }
            else -> {}
        }
    }
}