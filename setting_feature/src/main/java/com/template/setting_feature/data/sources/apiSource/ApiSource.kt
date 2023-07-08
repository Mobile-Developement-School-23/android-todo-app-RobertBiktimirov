package com.template.setting_feature.data.sources.apiSource

import com.template.api.entity.YandexAccountApi
import com.template.common.utli.ApiResult
import com.template.setting_feature.domain.entity.YandexAccountEntity
import retrofit2.Response

interface ApiSource {

    suspend fun getDataYandexAccount(): ApiResult<YandexAccountApi>
}