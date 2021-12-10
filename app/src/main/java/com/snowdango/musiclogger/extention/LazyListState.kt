package com.snowdango.musiclogger.extention

import androidx.compose.foundation.lazy.LazyListState


fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

fun LazyListState.isScrolledToStart() =
    layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0

