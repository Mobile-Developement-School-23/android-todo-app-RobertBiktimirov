package com.template.todoapp.ui.token

import android.content.Context
import android.content.SharedPreferences
import com.template.api.ApiTokenProvider
import com.template.api.ApiTokenProvider.Companion.token
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.template.resourses_module.R as resourceModule

class TokenProvider @Inject constructor(val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(resourceModule.string.name_toke_shared_preference),
            Context.MODE_PRIVATE
        )
    }

    fun getToken(): String? =
        sharedPreferences.getString(context.getString(resourceModule.string.key_sp_token), null)

    fun saveToken(
        token: YandexAuthToken,
        yandexAuthSdk: YandexAuthSdk,
        lambdaStartFragment: (() -> Unit)
    ) {

        CoroutineScope(Dispatchers.IO).launch {
            with(sharedPreferences.edit()) {
                putString(context.getString(resourceModule.string.key_sp_token), token.value)
                putString(
                    context.getString(resourceModule.string.key_sp_jwt_token),
                    yandexAuthSdk.getJwt(token)
                )
                apply()
            }
        }

        setToken(token.value, lambdaStartFragment)
    }


    fun setToken(token: String, lambdaStartFragment: (() -> Unit)) {
        ApiTokenProvider.token = token
        lambdaStartFragment()
    }
}