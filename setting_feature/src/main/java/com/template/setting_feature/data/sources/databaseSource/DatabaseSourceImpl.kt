package com.template.setting_feature.data.sources.databaseSource

import com.template.database.dao.YandexAccountDao
import com.template.setting_feature.data.mappers.toEntity
import com.template.setting_feature.domain.entity.YandexAccountEntity
import javax.inject.Inject

class DatabaseSourceImpl @Inject constructor(
    private val yandexAccountDao: YandexAccountDao
): DatabaseSource {
    override suspend fun getDataYandexAccount(): YandexAccountEntity {
        return yandexAccountDao.getDataYandexAccountCache().toEntity()
    }
}