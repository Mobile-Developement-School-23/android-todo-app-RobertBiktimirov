package com.template.setting_feature.di.modules

import com.template.database.AppDatabase
import com.template.database.dao.YandexAccountDao
import com.template.setting_feature.data.sources.apiSource.ApiSource
import com.template.setting_feature.data.sources.apiSource.ApiSourceImpl
import com.template.setting_feature.data.sources.databaseSource.DatabaseSource
import com.template.setting_feature.data.sources.databaseSource.DatabaseSourceImpl
import com.template.setting_feature.data.repostiry.SettingRepositoryImpl
import com.template.setting_feature.di.SettingScope
import com.template.setting_feature.domain.repository.SettingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [SettingModule.BindModule::class])
class SettingModule {


    @SettingScope
    @Provides
    fun provideYandexAccountDao(appDatabase: AppDatabase): YandexAccountDao =
        appDatabase.yandexAccountDao()

    @Module
    interface BindModule {

        @Binds
        @SettingScope
        fun bindSettingRepository(impl: SettingRepositoryImpl): SettingRepository

        @Binds
        @SettingScope
        fun bindApiSource(impl: ApiSourceImpl): ApiSource

        @Binds
        @SettingScope
        fun bindDatabaseSource(impl: DatabaseSourceImpl): DatabaseSource
    }

}