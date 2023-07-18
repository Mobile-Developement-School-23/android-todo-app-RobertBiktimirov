package com.template.setting_feature.domain.repository

import com.template.common.utli.RepositoryResult
import com.template.setting_feature.domain.entity.YandexAccountEntity
import com.template.setting_feature.ui.models.ThemeModel
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getTheme(): Flow<ThemeModel>

    suspend fun saveTheme(themeModel: ThemeModel)

    suspend fun getDataYandexAccount(): RepositoryResult<YandexAccountEntity>

}