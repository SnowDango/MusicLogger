package com.snowdango.musiclogger.domain.response.apple

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleSearchResult(
    @JsonProperty("artistId") val artistId: Int?,
    @JsonProperty("artistName") val artistName: String?,
    @JsonProperty("artistViewUrl") val artistViewUrl: String?,
    @JsonProperty("artworkUrl100") val artworkUrl100: String?,
    @JsonProperty("artworkUrl30") val artworkUrl30: String?,
    @JsonProperty("artworkUrl60") val artworkUrl60: String?,
    @JsonProperty("collectionArtistId") val collectionArtistId: String?,
    @JsonProperty("collectionArtistName") val collectionArtistName: String?,
    @JsonProperty("collectionCensoredName") val collectionCensoredName: String?,
    @JsonProperty("collectionExplicitness") val collectionExplicitness: String?,
    @JsonProperty("collectionId") val collectionId: Int?,
    @JsonProperty("collectionName") val collectionName: String?,
    @JsonProperty("collectionPrice") val collectionPrice: Double?,
    @JsonProperty("collectionViewUrl") val collectionViewUrl: String?,
    @JsonProperty("country") val country: String?,
    @JsonProperty("currency") val currency: String?,
    @JsonProperty("discCount") val discCount: Int?,
    @JsonProperty("discNumber") val discNumber: Int?,
    @JsonProperty("isStreamable") val isStreamable: Boolean?,
    @JsonProperty("kind") val kind: String?,
    @JsonProperty("previewUrl") val previewUrl: String?,
    @JsonProperty("primaryGenreName") val primaryGenreName: String?,
    @JsonProperty("releaseDate") val releaseDate: String?,
    @JsonProperty("trackCensoredName") val trackCensoredName: String?,
    @JsonProperty("trackCount") val trackCount: Int?,
    @JsonProperty("trackExplicitness") val trackExplicitness: String?,
    @JsonProperty("trackId") val trackId: Int?,
    @JsonProperty("trackName") val trackName: String?,
    @JsonProperty("trackNumber") val trackNumber: Int?,
    @JsonProperty("trackPrice") val trackPrice: Double?,
    @JsonProperty("trackTimeMillis") val trackTimeMillis: Int?,
    @JsonProperty("trackViewUrl") val trackViewUrl: String?,
    @JsonProperty("wrapperType") val wrapperType: String?
)