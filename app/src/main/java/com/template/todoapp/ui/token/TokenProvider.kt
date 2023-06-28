package com.template.todoapp.ui.token

import android.content.Context
import android.content.SharedPreferences
import com.template.api.ApiTokenProvider
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

    fun saveToken(token: String, lambdaStartFragment: (() -> Unit)) {

        with(sharedPreferences.edit()) {
            putString(context.getString(resourceModule.string.key_sp_token), token)
            commit()
        }

        setToken(token, lambdaStartFragment)
    }


    fun setToken(token: String, lambdaStartFragment: (() -> Unit)) {
        ApiTokenProvider.token = token
        lambdaStartFragment()
    }
}