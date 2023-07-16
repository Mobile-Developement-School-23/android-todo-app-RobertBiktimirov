package com.template.sign_up_feature.di

import androidx.lifecycle.ViewModel
import com.template.sign_up_feature.di.deps.SignDeps
import com.template.sign_up_feature.di.deps.SignDepsProvider
import com.template.sign_up_feature.di.modules.SignModule
import com.template.sign_up_feature.di.modules.YandexLoginModule
import com.template.sign_up_feature.di.modules.viewmodels.ViewModelModule
import com.template.sign_up_feature.ui.SignFragment
import dagger.Component
import retrofit2.http.Query
import javax.inject.Qualifier
import javax.inject.Scope

@SignScope
@Component(
    modules = [SignModule::class, ViewModelModule::class, YandexLoginModule::class],
    dependencies = [SignDeps::class]
)
interface SignComponent {

    fun inject(signFragment: SignFragment)

    @Component.Builder
    interface Builder {

        fun deps(signDeps: SignDeps): Builder
        fun build(): SignComponent

    }

}


@Scope
annotation class SignScope

@Qualifier
annotation class TokenSharedPreference

@Qualifier
annotation class YandexLoginIntent


class SignComponentViewModel : ViewModel() {

    val component = DaggerSignComponent.builder().deps(SignDepsProvider.deps).build()

}