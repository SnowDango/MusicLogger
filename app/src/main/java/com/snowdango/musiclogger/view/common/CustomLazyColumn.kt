package com.snowdango.musiclogger.view.common

import android.util.Log
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snowdango.musiclogger.extention.isScrolledToEnd
import com.snowdango.musiclogger.extention.isScrolledToStart

private var isStartFnAlready = false
private var isMiddleFnAlready = false
private var isEndFnAlready = false

@Composable
fun CostomLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    startFn: () -> Unit = {},
    middleFn: () -> Unit = {},
    endFn: () -> Unit = {},
    content: LazyListScope.() -> Unit
) {

    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior
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