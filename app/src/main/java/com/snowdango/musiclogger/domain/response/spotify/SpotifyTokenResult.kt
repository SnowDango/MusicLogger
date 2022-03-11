package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SpotifyTokenResult(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
    @JsonProperty("token_type") val tokenType: String?,
    @JsonProperty("error") val error: String?,
    @JsonProperty("error_description") val errorDescription: String?
)