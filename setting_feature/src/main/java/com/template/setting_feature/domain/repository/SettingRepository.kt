package com.template.setting_feature.domain.repository

import com.template.common.utli.RepositoryResult
import com.template.setting_feature.domain.entity.YandexAccountEntity

interface SettingRepository {
    suspend fun getDataYandexAccount(): RepositoryResult<YandexAccountEntity>

}