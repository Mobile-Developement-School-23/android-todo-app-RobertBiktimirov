package com.template.setting_feature.data.repostiry

import com.template.common.theme.ThemeEnumCommon
import com.template.common.theme.ThemeProvider
import com.template.common.utli.ApiError
import com.template.common.utli.ApiException
import com.template.common.utli.ApiSuccess
import com.template.common.utli.RepositoryError
import com.template.common.utli.RepositoryException
import com.template.common.utli.RepositoryResult
import com.template.common.utli.RepositorySuccess
import com.template.setting_feature.data.mappers.toEntity
import com.template.setting_feature.data.sources.apiSource.ApiSource
import com.template.setting_feature.data.sources.databaseSource.DatabaseSource
import com.template.setting_feature.data.utils.YANDEX_JWT_NULL_CODE
import com.template.setting_feature.domain.entity.YandexAccountEntity
import com.template.setting_feature.domain.repository.SettingRepository
import com.template.setting_feature.ui.models.ThemeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val apiSource: ApiSource,
    private val databaseSource: DatabaseSource,
    private val themeProvider: ThemeProvider
) : SettingRepository {
    override fun getTheme(): Flow<ThemeModel> {
        return themeProvider.getTheme().map {
            when (it) {
                ThemeEnumCommon.DARK -> ThemeModel.DARK
                ThemeEnumCommon.DAY -> ThemeModel.DAY
                ThemeEnumCommon.SYSTEM -> ThemeModel.SYSTEM
            }
        }
    }

    override suspend fun saveTheme(themeModel: ThemeModel) {
        themeProvider.setTheme(
            when(themeModel) {
                ThemeModel.DARK -> ThemeEnumCommon.DARK
                ThemeModel.DAY -> ThemeEnumCommon.DAY
                ThemeModel.SYSTEM -> ThemeEnumCommon.SYSTEM
            }
        )
    }

    override suspend fun getDataYandexAccount(): RepositoryResult<YandexAccountEntity> {

        val databaseAnswer = databaseSource.getDataYandexAccount()
            ?: when (val apiResponse = apiSource.getDataYandexAccount()) {
                is ApiError -> {

                    if (apiResponse.code == YANDEX_JWT_NULL_CODE) {
                        return RepositoryError(apiResponse.code, apiResponse.message)
                    }

                    return RepositoryError(apiResponse.code, apiResponse.message)
                }

                is ApiException -> {
                    return RepositoryException(apiResponse.e)
                }

                is ApiSuccess -> {
                    databaseSource.saveDataYandexAccount(apiResponse.data.toEntity())
                    return RepositorySuccess(apiResponse.data.toEntity())
                }
            }

        return RepositorySuccess(databaseAnswer)
    }
}