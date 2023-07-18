package com.template.sign_up_feature.data.sources.token_sources

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.template.api.ApiTokenProvider
import com.template.sign_up_feature.di.TokenSharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokenSourceImpl @Inject constructor(
    @TokenSharedPreference private val sharedPreferences: SharedPreferences,
    private val context: Context
) : TokenSource {

    override suspend fun getToken(): String? {
        val token = sharedPreferences.getString(
            context.getString(com.template.resourses_module.R.string.key_sp_token),
            null
        )
        if (token != null) {
            Log.d("nullTokenTest", "save token: $token")
            provideTokenInApi(token)
        }

        return token
    }


    override fun saveToken(token: String, jwtToken: String) {
        provideTokenInApi(token)
        CoroutineScope(Dispatchers.IO).launch {
            with(sharedPreferences.edit()) {
                putString(
                    context.getString(com.template.resourses_module.R.string.key_sp_token),
                    token
                )
                putString(
                    context.getString(com.template.resourses_module.R.string.key_sp_jwt_token),
                    jwtToken
                )
                apply()
            }
        }
    }

    private fun provideTokenInApi(token: String) {
        ApiTokenProvider.token = token
    }
}