package com.snowdango.musiclogger.repository.api

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object BaseApiProvider {

    fun provideContributorsApi(baseUrl: String, requestInterceptor: Interceptor?) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(provideOkHttpClient(requestInterceptor, provideLoggingInterceptor()))
        .addConverterFactory(
            JacksonConverterFactory.create(ObjectMapper())
        ).build()

    private fun provideOkHttpClient(
        requestInterceptor: Interceptor?,
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().also {
            it.socketFactory(CustomSocketFactory())
            requestInterceptor?.let { it1 -> it.addInterceptor(it1) }
            it.addInterceptor(interceptor)
        }.socketFactory(CustomSocketFactory())
        return builder.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }
}