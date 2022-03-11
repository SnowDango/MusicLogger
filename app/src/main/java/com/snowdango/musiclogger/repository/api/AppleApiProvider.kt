package com.snowdango.musiclogger.repository.api

import com.snowdango.musiclogger.APPLE_API
import com.snowdango.musiclogger.repository.api.musicapi.AppleApi
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*

object AppleApiProvider {

    val appleApi: AppleApi by lazy {
        BaseApiProvider.provideContributorsApi(APPLE_API, RequestInterceptor).create(AppleApi::class.java)
    }

    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val httpUrl = request.url.newBuilder().apply {
                addQueryParameter("lang", languageApple())
                addQueryParameter("country", Locale.getDefault().country.lowercase())
                Timber.d("lang: ${languageApple()}, country: ${Locale.getDefault().country.lowercase()}")
            }.build()
            val builder = request.newBuilder().apply {
                addHeader("Content-Type", "application/json")
            }
            val req = builder.url(httpUrl).build()
            return chain.proceed(req)
        }
    }

    private fun languageApple(): String {
        val country = Locale.getDefault().country
        return if (country == "jp") {
            "ja_jp"
        } else {
            "en_us"
        }
    }

}