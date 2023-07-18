package com.template.todoapp.di.modules

import android.content.Context
import com.template.common.theme.ThemeProvider
import com.template.common.theme.ThemeProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface ThemeModule {
    @Binds
    @Singleton
    fun bindThemeProvider(impl: ThemeProviderImpl): ThemeProvider

    companion object {
        @Provides
        @Singleton
        fun provideThemeProviderImpl(context: Context): ThemeProviderImpl {
            return ThemeProviderImpl(context)
        }
    }

}