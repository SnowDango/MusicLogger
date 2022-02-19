package com.snowdango.musiclogger.ui.history

import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.item.historyView

class HistoryEpoxyController : TypedEpoxyController<ModelState<List<MusicMetaWithArt>>>() {

    override fun buildModels(data: ModelState<List<MusicMetaWithArt>>) {
        when (data) {
            is ModelState.Success -> {
                val historyList = data.data
                historyList.forEachIndexed { index, musicMetaWithArt ->
                    Log.d("historyView", "${musicMetaWithArt.id}")
                    historyView {
                        id(index)
                        musicMetaWithArt(musicMetaWithArt)
                    }
                }
            }
            else -> {}
        }
    }
}