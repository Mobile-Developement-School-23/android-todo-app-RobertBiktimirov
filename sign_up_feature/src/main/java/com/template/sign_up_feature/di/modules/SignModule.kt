package com.template.sign_up_feature.di.modules

import com.template.sign_up_feature.data.repository.SignRepositoryImpl
import com.template.sign_up_feature.data.sources.token_sources.TokenSource
import com.template.sign_up_feature.data.sources.token_sources.TokenSourceImpl
import com.template.sign_up_feature.di.SignScope
import com.template.sign_up_feature.domain.repository.SignRepository
import dagger.Binds
import dagger.Module

@Module(includes = [SignModule.BindModule::class])
class SignModule {


    @Module
    interface BindModule {

        @Binds
        @SignScope
        fun bindSignRepository(impl: SignRepositoryImpl): SignRepository

        @Binds
        @SignScope
        fun bindTokenSource(impl: TokenSourceImpl): TokenSource

    }


}