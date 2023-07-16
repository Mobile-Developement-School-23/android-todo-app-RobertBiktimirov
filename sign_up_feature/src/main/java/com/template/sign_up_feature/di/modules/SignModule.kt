package com.template.sign_up_feature.di.modules

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.template.sign_up_feature.data.repository.SignRepositoryImpl
import com.template.sign_up_feature.data.sources.token_sources.TokenSource
import com.template.sign_up_feature.data.sources.token_sources.TokenSourceImpl
import com.template.sign_up_feature.data.sources.yandex_sources.YandexJwtTokenSource
import com.template.sign_up_feature.data.sources.yandex_sources.YandexJwtTokenSourceImpl
import com.template.sign_up_feature.di.SignScope
import com.template.sign_up_feature.di.TokenSharedPreference
import com.template.sign_up_feature.domain.repository.SignRepository
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [SignModule.BindModule::class])
class SignModule {


    @Provides
    @SignScope
    @TokenSharedPreference
    fun provideTokenSharedPreference(context: Context): SharedPreferences =
        context.getSharedPreferences(
            context.getString(com.template.resourses_module.R.string.name_toke_shared_preference),
            Context.MODE_PRIVATE
        )


    @Module
    interface BindModule {

        @Binds
        @SignScope
        fun bindSignRepository(impl: SignRepositoryImpl): SignRepository

        @Binds
        @SignScope
        fun bindTokenSource(impl: TokenSourceImpl): TokenSource

        @Binds
        @SignScope
        fun bindYandexJwtTokenSource(impl: YandexJwtTokenSourceImpl): YandexJwtTokenSource

    }


}