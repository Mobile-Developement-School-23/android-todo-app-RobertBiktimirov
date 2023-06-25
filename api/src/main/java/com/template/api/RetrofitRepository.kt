package com.template.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.template.api.interceptors.AuthInterceptor
import com.template.api.interceptors.CacheInterceptor
import com.template.api.services.TodoService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
class RetrofitRepository(context: Context) {

    companion object {
        private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
        private const val CONNECT_TIME = 5000L
        private const val WRITE_TIME = 5000L
        private const val READE_TIME = 5000L
    }

    private val cacheSize = (5 * 1024 * 1024).toLong()

    private val cache = Cache(context.cacheDir, cacheSize)

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(HttpLoggingInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(AuthInterceptor())
        .addInterceptor(CacheInterceptor(context))
        .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
        .readTimeout(READE_TIME, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()


    val api: TodoService = retrofit.create(TodoService::class.java)

}