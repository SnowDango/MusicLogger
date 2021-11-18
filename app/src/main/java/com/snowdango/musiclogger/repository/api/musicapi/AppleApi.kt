package com.snowdango.musiclogger.repository.api.musicapi

import com.snowdango.musiclogger.domain.response.AppleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleApi {

    @GET("search")
    fun getSongInfo(
        @Query("term") mediaId: String
    ):Call<AppleResponse>

}