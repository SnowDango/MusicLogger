package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonProperty

data class ExternalIds(
    @JsonProperty("isrc") val isrc: String?
)
