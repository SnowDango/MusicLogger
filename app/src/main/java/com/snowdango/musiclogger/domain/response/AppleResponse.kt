package com.snowdango.musiclogger.domain.response

data class AppleResponse(
    val resultCount: Int,
    val appleSearchResults: List<AppleSearchResult>
)