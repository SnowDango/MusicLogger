package com.snowdango.musiclogger.view.common

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
import timber.log.Timber

private var isStartFnAlready = false
private var isMiddleFnAlready = false
private var isEndFnAlready = false
private var isNotFilledFnAlready = false

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
    notFilled: () -> Unit = {},
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
        val isStart = state.isScrolledToStart()
        val isEnd = state.isScrolledToEnd()
        if (isStart && isEnd) {
            if (!isNotFilledFnAlready) {
                Timber.tag("scroll").d("not filled")
                isNotFilledFnAlready = true
                notFilled()
                isMiddleFnAlready = false
                isEndFnAlready = false
                isStartFnAlready = false
            }
        } else if (isStart) {
            if (!isStartFnAlready) {
                Timber.tag("scroll").d("start")
                startFn()
                isStartFnAlready = true
                isMiddleFnAlready = false
            }
        } else if (isEnd) {
            if (!isEndFnAlready) {
                Timber.tag("scroll").d("end")
                endFn()
                isEndFnAlready = true
                isMiddleFnAlready = false
            }
        } else {
            if (!isMiddleFnAlready) {
                Timber.tag("scroll").d("middle")
                middleFn()
                isMiddleFnAlready = true
                isStartFnAlready = false
                isEndFnAlready = false
            }
        }
    }
}