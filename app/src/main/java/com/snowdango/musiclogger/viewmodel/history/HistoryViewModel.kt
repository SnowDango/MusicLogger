package com.snowdango.musiclogger.viewmodel.history

import androidx.lifecycle.*
import com.snowdango.musiclogger.domain.session.MusicMeta
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.model.MusicHistoryModel
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.repository.ontime.NowPlayData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : ViewModel(), KoinComponent {

    private val musicHistoryModel by inject<MusicHistoryModel>()
    val historyData = MutableLiveData<ModelState<List<MusicMetaWithArt>>>()
    val mutex: Mutex = Mutex()
    private var observer: Observer<MusicMeta> = Observer {
        firstFetch()
    }

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

    fun autoUpdate(lifecycleOwner: LifecycleOwner) = viewModelScope.launch {
        mutex.withLock {
            NowPlayData.musicMeta.observe(lifecycleOwner, observer)
        }
    }

    fun cancelAutoUpdate() = viewModelScope.launch {
        mutex.withLock {
            NowPlayData.musicMeta.removeObserver(observer)
        }
    }
}