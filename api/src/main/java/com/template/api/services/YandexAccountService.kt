package com.template.api.services

import com.template.api.entity.YandexAccountApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexAccountService {

    @GET("info")
    suspend fun getYandexAccount(
        @Query("format") format: String = "json",
        @Query("jwt_secret") jwt: String
    ): Response<YandexAccountApi>

}