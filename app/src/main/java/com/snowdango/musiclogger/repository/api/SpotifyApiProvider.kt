package com.snowdango.musiclogger.repository.api

import android.content.Context
import android.widget.Toast
import com.snowdango.musiclogger.*
import com.snowdango.musiclogger.extention.spotifyToken
import com.snowdango.musiclogger.extention.spotifyTokenLastUpdate
import com.snowdango.musiclogger.repository.api.spotify.SpotifyApi
import com.snowdango.musiclogger.repository.api.spotify.SpotifyTokenApi
import com.soywiz.klock.DateTime
import com.soywiz.klock.hours
import com.soywiz.klock.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

object SpotifyApiProvider : KoinComponent {

    private val context: Context by inject()

    suspend fun authorizedSpotifyApi(): SpotifyApi? {
        authorize()
        return spotifyApi
    }

    private val mutex = Mutex()
    private suspend fun authorize() = mutex.withLock {
        val nowTime = DateTime.now()
        val lastTime = DateTime.fromUnix(App.preferences!!.spotifyTokenLastUpdate)
        if (lastTime + 1.hours >= nowTime) { // refresh
            callTokenApi()
        } else {
            if (spotifyApi == null) {
                callTokenApi()
            }
        }
    }

    private fun callTokenApi() {
        try {
            spotifyApi = null
            val result = spotifyTokenApi.generateToken().execute()
            result.body()?.accessToken?.let {
                App.preferences!!.spotifyTokenLastUpdate = (DateTime.now() - 10.minutes).unixMillisLong
                App.preferences!!.spotifyToken = it
                spotifyApi = BaseApiProvider.provideContributorsApi(SPOTIFY_API, RequestInterceptor)
                    .create(SpotifyApi::class.java)
            }
            result.body()?.error?.let {
                Toast.makeText(context, "spotify token api error: $it", Toast.LENGTH_SHORT).show()
                App.preferences!!.spotifyToken = ""
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "spotify token api error: ${context.resources.getString(R.string.spotify_token_error)}",
                Toast.LENGTH_SHORT
            ).show()
            App.preferences!!.spotifyToken = ""
        }
    }

    private var spotifyApi: SpotifyApi? = null
    private val spotifyTokenApi: SpotifyTokenApi by lazy {
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
            val builder = request.newBuilder().addHeader("Authorization", "Bearer ${App.preferences!!.spotifyToken}")
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