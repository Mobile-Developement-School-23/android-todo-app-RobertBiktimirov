package com.template.sign_up_feature.data.repository

import android.util.Log
import com.template.sign_up_feature.data.sources.token_sources.TokenSource
import com.template.sign_up_feature.domain.repository.SignRepository
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val tokenSource: TokenSource,
    private val yandexAuthSdk: YandexAuthSdk
) : SignRepository {

    override suspend fun getToken(): String? {
        return tokenSource.getToken()
    }

    override suspend fun saveToken(token: YandexAuthToken): String {
        val jwtToken = yandexAuthSdk.getJwt(token)

        return if (jwtToken != "" && token.value != "") {
            Log.d("tokensTest", "${token.value} \n$jwtToken")
            tokenSource.saveToken(token.value, jwtToken)
            token.value
        } else {
            ""
        }
    }
}