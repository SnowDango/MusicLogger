package com.snowdango.musiclogger.domain.response.apple

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleResponse(
    @JsonProperty("resultCount") val resultCount: Int,
    @JsonProperty("results") val appleSearchResults: List<AppleSearchResult>
)