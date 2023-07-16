package com.template.sign_up_feature.domain.repository

import com.yandex.authsdk.YandexAuthToken

interface SignRepository {

    suspend fun getToken(): String?

    suspend fun saveToken(token: YandexAuthToken): String
}