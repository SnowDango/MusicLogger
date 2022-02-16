package com.snowdango.musiclogger.view.history

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.MusicMetaWithArt
import com.snowdango.musiclogger.view.common.CostomLazyColumn
import com.snowdango.musiclogger.view.common.CustomAppBar
import com.snowdango.musiclogger.view.common.items.ListMusicItem
import com.snowdango.musiclogger.viewmodel.history.HistoryViewModel
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(),
    context: Context = get(),
    startFn: () -> Unit,
    middleFn: () -> Unit,
    endFn: () -> Unit
) {
    val state = viewModel.historyData.observeAsState()
    Scaffold(
        topBar = {
            CustomAppBar(icon = Icons.Filled.Settings) {
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        containerColor = colorResource(R.color.backGround)
    ) {
        when (state.value) {
            null -> return@Scaffold
            is ModelState.Success -> {
                val histories = (state.value as ModelState.Success<List<MusicMetaWithArt>>).data
                CostomLazyColumn(
                    modifier = Modifier
                        .background(colorResource(R.color.backGround)),
                    startFn = startFn,
                    middleFn = middleFn,
                    endFn = endFn,
                    notFilled = startFn
                ) {
                    items(histories) { data ->
                        ListMusicItem(musicMetaWithArt = data)
                    }
                }
            }
            else -> {}
        }
    }
}
