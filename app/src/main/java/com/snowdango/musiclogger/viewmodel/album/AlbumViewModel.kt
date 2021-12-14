package com.snowdango.musiclogger.viewmodel.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowdango.musiclogger.model.HistoryAlbumModel
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlbumViewModel : ViewModel(), KoinComponent {

    private val albumModel by inject<HistoryAlbumModel>()
    val albumData = MutableLiveData<ModelState<List<AlbumWithArt>>>()
    private val mutex: Mutex = Mutex()

    fun firstFetch() = viewModelScope.launch(Dispatchers.IO) {
        val result = albumModel.getAlbum()
        withContext(Dispatchers.Default) {
            albumData.postValue(result)
        }
    }

    fun moreFetch() = viewModelScope.launch {
        val result = albumModel.getMoreAlbum(albumData.value ?: ModelState.Loading)
        albumData.postValue(result)
    }

}