package com.snowdango.musiclogger.view.history

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.common.ListMusicItem
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel

@Preview
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel()
) {
    val state = viewModel.historyData.observeAsState()
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()
    when (state.value) {
        null -> return
        is ModelState.Success -> {
            val histories = (state.value as ModelState.Success<List<MusicMetaWithArt>>).data
            LazyColumn(
                modifier = Modifier
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = scrollState,
                    ),
                state = listState
            ) {
                items(histories) { data ->
                    ListMusicItem(musicMetaWithArt = data)
                }
            }
        }
    }
}