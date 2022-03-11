package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalIds(
    @JsonProperty("isrc") val isrc: String?
)
