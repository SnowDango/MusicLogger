package com.snowdango.musiclogger.usecase.api

import com.snowdango.musiclogger.domain.response.AppleSearchResult
import com.snowdango.musiclogger.repository.api.ApiProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchAppleMusicData {

    suspend fun getMusicForMediaId(mediaId: String): AppleSearchResult? = withContext(Dispatchers.Default) {
        try {
            val apiResult = ApiProvider.appleApi.getSongInfo(mediaId).execute()
            if (apiResult.isSuccessful) {
                apiResult.body()?.resultCount?.let {
                    if (it > 0) {
                        return@withContext apiResult.body()!!.appleSearchResults[0]
                    }
                }
            }
            return@withContext null
        } catch (e: Exception) {
            return@withContext null
        }
    }
}