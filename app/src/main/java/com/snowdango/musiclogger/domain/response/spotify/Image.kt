package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonProperty

data class Image(
    @JsonProperty("height") val height: Int,
    @JsonProperty("url") val url: String,
    @JsonProperty("width") val width: Int
)
