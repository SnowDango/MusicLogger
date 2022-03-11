package com.snowdango.musiclogger.repository.api.spotify

import com.snowdango.musiclogger.domain.response.spotify.SpotifyTokenResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyTokenApi {

    @FormUrlEncoded
    @POST("token")
    fun generateToken(
        @Field("grant_type") grantType: String
    ): Call<SpotifyTokenResult>

}