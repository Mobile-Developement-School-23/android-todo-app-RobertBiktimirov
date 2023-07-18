package com.template.sign_up_feature.di.modules

import android.content.Context
import android.content.Intent
import com.template.sign_up_feature.di.SignScope
import com.template.sign_up_feature.di.YandexLoginIntent
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides

@Module
class YandexLoginModule {

    @Provides
    @SignScope
    fun provideYandexSdk(context: Context): YandexAuthSdk {
        return YandexAuthSdk(context, YandexAuthOptions(context))
    }

    @Provides
    @SignScope
    fun provideYandexLoginOptionBuilder(): YandexAuthLoginOptions.Builder =
        YandexAuthLoginOptions.Builder()

    @Provides
    @SignScope
    @YandexLoginIntent
    fun provideYandexLoginIntent(
        yandexSdk: YandexAuthSdk,
        loginOptionBuilder: YandexAuthLoginOptions.Builder
    ): Intent =
        yandexSdk.createLoginIntent(loginOptionBuilder.build())

}