package com.snowdango.musiclogger.repository.api

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.snowdango.musiclogger.APPLE_API
import com.snowdango.musiclogger.repository.api.musicapi.AppleApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.InetAddress
import java.net.Socket
import java.util.*
import javax.net.SocketFactory

object ApiProvider {

    val appleApi: AppleApi by lazy { provideContributorsApi().create(AppleApi::class.java) }

    private fun provideContributorsApi() = Retrofit.Builder()
        .baseUrl(APPLE_API)
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addConverterFactory(
            JacksonConverterFactory.create(ObjectMapper())
        ).build()

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            socketFactory(CustomSocketFactory())
            addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    if (chain.request().url.pathSegments[0] != "api") {
                        return chain.proceed(chain.request())
                    }

                    val request = chain.request()

                    val httpUrl = request.url.newBuilder().apply {
                        addQueryParameter("lang", languageApple())
                        addQueryParameter("country", Locale.getDefault().country.lowercase())
                        Log.d("api", "lang: ${languageApple()}, country: ${Locale.getDefault().country.lowercase()}")
                    }.build()
                    val builder = request.newBuilder()
                    val req = builder.url(httpUrl).build()
                    return chain.proceed(req)
                }
            })
            addInterceptor(interceptor)
        }.socketFactory(CustomSocketFactory())
        return builder.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
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

class CustomSocketFactory : SocketFactory() {

    override fun createSocket(): Socket {
        val socket = Socket()
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: String?, port: Int): Socket {
        val socket = Socket(host, port)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        val socket = Socket(host, port, localHost, localPort)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        val socket = Socket(host, port)
        socket.tcpNoDelay = true
        return socket
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        val socket = Socket(address, port, localAddress, localPort)
        socket.tcpNoDelay = true
        return socket
    }

}