package com.snowdango.musiclogger.view.album

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snowdango.musiclogger.R
import com.snowdango.musiclogger.model.ModelState
import com.snowdango.musiclogger.repository.db.dao.entity.AlbumWithArt
import com.snowdango.musiclogger.view.common.CustomAppBar
import com.snowdango.musiclogger.view.common.CustomLazyRow
import com.snowdango.musiclogger.view.common.GridAlbumItem
import com.snowdango.musiclogger.viewmodel.album.AlbumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = viewModel(),
    startFn: () -> Unit = {},
    middleFn: () -> Unit = {},
    endFn: () -> Unit = {}
) {
    val state = viewModel.albumData.observeAsState()
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
                val albums = (state.value as ModelState.Success<List<AlbumWithArt>>).data
                val spanCount = 2
                CustomLazyRow(
                    modifier = Modifier
                        .background(colorResource(R.color.backGround)),
                    startFn = startFn,
                    middleFn = middleFn,
                    endFn = endFn
                ) {
                    items(albums) { album ->
                        GridAlbumItem(album, spanCount = spanCount)
                    }
                }
            }
            else -> {}
        }
    }
}