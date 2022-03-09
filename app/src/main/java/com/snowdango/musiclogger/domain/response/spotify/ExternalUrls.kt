package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonProperty

data class ExternalUrls(
    @JsonProperty("spotify") val spotify: String?
)
