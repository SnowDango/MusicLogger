package com.snowdango.musiclogger.viewmodel.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.model.MusicHistoryModel
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : ViewModel(), KoinComponent {

    private val musicHistoryModel by inject<MusicHistoryModel>()

    val historyData = MutableLiveData<ModelState<List<MusicMetaWithArt>>>()

    fun firstFetch() = viewModelScope.launch(Dispatchers.IO) {
        val result = musicHistoryModel.getMusicHistory()
        withContext(Dispatchers.Default) {
            historyData.postValue(result)
        }
    }

    fun moreFetch() = viewModelScope.launch {
        val result = musicHistoryModel.getMoreFetchHistory(historyData.value ?: ModelState.Loading)
        historyData.postValue(result)
    }

}