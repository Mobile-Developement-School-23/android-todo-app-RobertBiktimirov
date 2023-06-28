package com.template.todoapp.ui.yandex

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import com.template.todoapp.ui.main.MainActivity
import com.template.todoapp.ui.token.TokenProvider
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

class YandexSignUpActivity @Inject constructor(
    private val tokenProvider: TokenProvider
) {

    private val context = tokenProvider.context
    private val token = tokenProvider.getToken()

    fun registerYandexSignUp(lambdaStartFragment: (() -> Unit)) {
        if (token == null) {
            val yandexSdk = YandexAuthSdk(context, YandexAuthOptions(context))

            val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
            val intent = yandexSdk.createLoginIntent(loginOptionsBuilder.build())

            (context as MainActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val yandexAuthToken = yandexSdk.extractToken(it.resultCode, it.data)
                    if (yandexAuthToken != null) {
                        tokenProvider.saveToken(yandexAuthToken.value, lambdaStartFragment)
                    }
                }
            }.launch(intent)
        } else {
            tokenProvider.setToken(token, lambdaStartFragment)
        }
    }
}