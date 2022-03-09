package com.snowdango.musiclogger.domain.response.spotify

import com.fasterxml.jackson.annotation.JsonProperty

data class Album(
    @JsonProperty("album_type") val albumType: String,
    @JsonProperty("artists") val artists: List<Artist>?,
    @JsonProperty("available_markets") val availableMarkets: List<String>?,
    @JsonProperty("external_urls") val externalUrls: ExternalUrls?,
    @JsonProperty("href") val href: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("images") val images: List<Image>?,
    @JsonProperty("name") val name: String,
    @JsonProperty("release_date") val releaseDate: String?,
    @JsonProperty("release_date_precision") val releaseDatePrecision: String?,
    @JsonProperty("total_tracks") val totalTracks: Int,
    @JsonProperty("type") val type: String,
    @JsonProperty("uri") val uri: String
)
