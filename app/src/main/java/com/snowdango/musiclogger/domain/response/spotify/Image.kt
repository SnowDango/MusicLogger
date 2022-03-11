package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Image(
    @JsonProperty("height") val height: Int,
    @JsonProperty("url") val url: String,
    @JsonProperty("width") val width: Int
)
