package com.snowdango.musiclogger.repository.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.snowdango.musiclogger.APPLE_API
import com.snowdango.musiclogger.repository.api.musicapi.AppleApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiProvider {

    val appleApi: AppleApi = provideContributorsApi().create(AppleApi::class.java)

    private fun provideContributorsApi() = Retrofit.Builder()
        .baseUrl(APPLE_API)
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addConverterFactory(
            JacksonConverterFactory.create(ObjectMapper())
        ).build()

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient{
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

}