package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SpotifyTracksResult(
    @JsonProperty("album") val album: Album?,
    @JsonProperty("artists") val artists: List<Artist>?,
    @JsonProperty("available_markets") val availableMarkets: List<String>?,
    @JsonProperty("disc_number") val diskNumber: Int?,
    @JsonProperty("duration_ms") val durationMs: Long?,
    @JsonProperty("explicit") val explicit: Boolean?,
    @JsonProperty("external_ids") val externalIds: ExternalIds?,
    @JsonProperty("external_urls") val externalUrls: ExternalUrls?,
    @JsonProperty("href") val href: String?,
    @JsonProperty("id") val id: String?,
    @JsonProperty("is_local") val isLocal: Boolean?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("popularity") val popularity: Int?,
    @JsonProperty("preview_url") val previewUrl: String?,
    @JsonProperty("track_number") val trackNumber: Int?,
    @JsonProperty("type") val type: String?,
    @JsonProperty("uri") val uri: String?,
    @JsonProperty("error") val error: Error?
)

data class Error(
    @JsonProperty("status") val status: Int,
    @JsonProperty("message") val message: String
)