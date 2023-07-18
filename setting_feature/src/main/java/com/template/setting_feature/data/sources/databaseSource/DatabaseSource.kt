package com.template.setting_feature.data.sources.databaseSource

import com.template.setting_feature.domain.entity.YandexAccountEntity

interface DatabaseSource {

    suspend fun getDataYandexAccount(): YandexAccountEntity?

    suspend fun saveDataYandexAccount(yandexAccountEntity: YandexAccountEntity)

}