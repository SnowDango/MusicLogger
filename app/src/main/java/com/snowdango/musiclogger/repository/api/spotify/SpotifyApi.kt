package com.snowdango.musiclogger.repository.api.spotify

import com.snowdango.musiclogger.domain.response.spotify.SpotifyTracksResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SpotifyApi {

    @GET("tracks/{id}")
    fun getTrack(
        @Path("id") id: String
    ): Call<SpotifyTracksResult>

}