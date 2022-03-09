package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyTokenResult(
    @JsonProperty("access_token") val accessToken: String?,
    @JsonProperty("expires_in") val expiresIn: Int?,
    @JsonProperty("token_type") val tokenType: String?,
    @JsonProperty("error") val error: String?,
    @JsonProperty("error_description") val errorDescription: String?
)