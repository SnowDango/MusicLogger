package com.snowdango.musiclogger.repository.api.spotify

import com.snowdango.musiclogger.domain.response.spotify.SpotifyTokenResult
import retrofit2.Call
import retrofit2.http.POST

interface SpotifyTokenApi {

    @POST("token")
    fun generateToken(): Call<SpotifyTokenResult>

}