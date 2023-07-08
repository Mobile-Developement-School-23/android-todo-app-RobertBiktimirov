package com.template.setting_feature.data.sources.apiSource

import android.util.Log
import com.template.api.entity.YandexAccountApi
import com.template.api.services.YandexAccountService
import com.template.common.utli.ApiError
import com.template.common.utli.ApiResult
import com.template.common.utli.handleApi
import com.template.setting_feature.data.sources.jwtTokenProvider.JwtTokenProvider
import com.template.setting_feature.data.utils.YANDEX_JWT_NULL_CODE
import com.template.setting_feature.data.utils.YANDEX_JWT_NULL_MESSAGE
import javax.inject.Inject

class ApiSourceImpl @Inject constructor(
    private val yandexAccountService: YandexAccountService,
    private val jwtTokenProvider: JwtTokenProvider
) : ApiSource {
    override suspend fun getDataYandexAccount(): ApiResult<YandexAccountApi> {
        val jwtToken = jwtTokenProvider.getJwtToken()

        Log.d("jwtTokenProvider.getJwtToken()", jwtToken.toString())
        return if (jwtToken == null) {
            return ApiError(YANDEX_JWT_NULL_CODE, YANDEX_JWT_NULL_MESSAGE)
        } else {
            handleApi { yandexAccountService.getYandexAccount(jwt = jwtToken) }
        }
    }

}