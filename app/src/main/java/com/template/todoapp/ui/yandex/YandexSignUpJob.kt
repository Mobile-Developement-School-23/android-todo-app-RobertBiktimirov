package com.template.todoapp.ui.yandex

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.template.todoapp.ui.token.TokenProvider
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject
import kotlin.properties.Delegates

class YandexSignUpJob @Inject constructor(
    private val tokenProvider: TokenProvider
) {

    private val context = tokenProvider.context
    private val token = tokenProvider.getToken()

    private var yandexSdk: YandexAuthSdk by Delegates.notNull()

    fun getYandexIntent(defaultIntent: Intent): Intent {

        return if (token == null) {
            yandexSdk = YandexAuthSdk(context, YandexAuthOptions(context))

            val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
            yandexSdk.createLoginIntent(loginOptionsBuilder.build())
        } else {
            defaultIntent
        }
    }

    fun registerYandexSignUp(
        it: ActivityResult,
        lambdaStartFragment: (() -> Unit),
        handlerError: (() -> Unit)
    ) {
        if (token == null) {
            if (it.resultCode == Activity.RESULT_OK) {
                val yandexAuthToken = yandexSdk.extractToken(it.resultCode, it.data)
                if (yandexAuthToken != null) {
                    tokenProvider.saveToken(yandexAuthToken.value, lambdaStartFragment)
                }
            }

        } else {
            tokenProvider.setToken(token, lambdaStartFragment)
        }
    }
}