package com.snowdango.musiclogger.repository.api

import com.snowdango.musiclogger.BuildConfig
import com.snowdango.musiclogger.SPOTIFY_API
import com.snowdango.musiclogger.SPOTIFY_TOKEN_API
import com.snowdango.musiclogger.repository.api.spotify.SpotifyApi
import com.snowdango.musiclogger.repository.api.spotify.SpotifyTokenApi
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*

object SpotifyApiProvider {

    val spotifyApi: SpotifyApi by lazy {
        BaseApiProvider.provideContributorsApi(SPOTIFY_API, RequestInterceptor).create(SpotifyApi::class.java)
    }
    val spotifyTokenApi: SpotifyTokenApi by lazy {
        BaseApiProvider.provideContributorsApi(
            SPOTIFY_TOKEN_API,
            TokenRequestInterceptor
        ).create(SpotifyTokenApi::class.java)
    }


    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val httpUrl = request.url.newBuilder().apply {
                addQueryParameter("market", Locale.getDefault().country)
                Timber.d("country: ${Locale.getDefault().country}")
            }.build()
            val builder = request.newBuilder()
            val req = builder.url(httpUrl).build()
            return chain.proceed(req)
        }
    }

    object TokenRequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val httpUrl = request.url.newBuilder().apply {
                addQueryParameter("grant_type", "client_credentials")
            }.build()
            val builder = request.newBuilder().addHeader("Authorization", "Basic ${BuildConfig.SPOTIFY_BASE64}")
            val req = builder.url(httpUrl).build()
            return chain.proceed(req)
        }
    }

}