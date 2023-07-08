package com.template.setting_feature.data.repostiry

import android.util.Log
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
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val apiSource: ApiSource,
    private val databaseSource: DatabaseSource
) : SettingRepository {

    override suspend fun getDataYandexAccount(): RepositoryResult<YandexAccountEntity> {

        val databaseAnswer = databaseSource.getDataYandexAccount()
            ?: when(val apiResponse = apiSource.getDataYandexAccount()){
                is ApiError -> {

                    if (apiResponse.code == YANDEX_JWT_NULL_CODE) {
                        Log.d("YANDEX_JWT_NULL_CODE", "null jwt")
                        return RepositoryError(apiResponse.code, apiResponse.message)
                    }

                    return RepositoryError(apiResponse.code, apiResponse.message)
                }
                is ApiException -> {
                    Log.d("getDataYandexAccount", "from repos ${apiResponse.e}")
                    return RepositoryException(apiResponse.e)
                }
                is ApiSuccess -> {
                    databaseSource.saveDataYandexAccount(apiResponse.data.toEntity())
                    return RepositorySuccess(apiResponse.data.toEntity())
                }
            }

        Log.d("getDataYandexAccountTest", databaseAnswer.toString())

        return RepositorySuccess(databaseAnswer)
    }
}