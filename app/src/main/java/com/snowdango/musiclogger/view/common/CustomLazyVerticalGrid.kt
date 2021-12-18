package com.snowdango.musiclogger.view.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snowdango.musiclogger.extention.isScrolledToEnd
import com.snowdango.musiclogger.extention.isScrolledToStart

private var isStartFnAlready = false
private var isMiddleFnAlready = false
private var isEndFnAlready = false


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomLazyVerticalGrid(
    cells: GridCells = GridCells.Fixed(2),
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    startFn: () -> Unit = {},
    middleFn: () -> Unit = {},
    endFn: () -> Unit = {},
    content: LazyGridScope.() -> Unit
) {

    LazyVerticalGrid(
        cells = cells,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
    ) {
        content()
        if (state.isScrolledToEnd()) {
            if (!isEndFnAlready) {
                Log.d("scroll", "end")
                endFn()
                isEndFnAlready = true
                isMiddleFnAlready = false
            }
        } else if (state.isScrolledToStart()) {
            if (!isStartFnAlready) {
                Log.d("scroll", "start")
                startFn()
                isStartFnAlready = true
                isMiddleFnAlready = false
            }
        } else {
            if (!isMiddleFnAlready) {
                Log.d("scroll", "middle")
                middleFn()
                isMiddleFnAlready = true
                isStartFnAlready = false
                isEndFnAlready = false
            }
        }
    }
}