package com.template.setting_feature.data.repostiry

import com.template.common.utli.RepositoryResult
import com.template.setting_feature.data.sources.apiSource.ApiSource
import com.template.setting_feature.data.sources.databaseSource.DatabaseSource
import com.template.setting_feature.domain.entity.YandexAccountEntity
import com.template.setting_feature.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val apiSource: ApiSource,
    private val databaseSource: DatabaseSource
) : SettingRepository {

    override suspend fun getDataYandexAccount(): RepositoryResult<YandexAccountEntity> {
        TODO("Not yet implemented")
    }
}