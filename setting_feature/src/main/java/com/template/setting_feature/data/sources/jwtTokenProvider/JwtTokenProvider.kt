package com.template.setting_feature.data.sources.jwtTokenProvider

import android.content.Context
import javax.inject.Inject
import com.template.resourses_module.R as resourceModule

class JwtTokenProvider @Inject constructor(
    private val context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(resourceModule.string.name_toke_shared_preference),
        Context.MODE_PRIVATE
    )

    suspend fun getJwtToken(): String? {
        return sharedPreferences.getString(
            context.getString(resourceModule.string.key_sp_jwt_token), null
        )
    }
}