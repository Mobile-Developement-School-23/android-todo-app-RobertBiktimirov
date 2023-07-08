package com.template.setting_feature.di

import androidx.lifecycle.ViewModel
import com.template.setting_feature.di.deps.SettingDeps
import com.template.setting_feature.di.deps.SettingDepsProvider
import com.template.setting_feature.di.modules.SettingModule
import com.template.setting_feature.di.modules.viewmodels.ViewModelModule
import com.template.setting_feature.ui.SettingFragment
import dagger.Component
import javax.inject.Scope

@SettingScope
@Component(
    modules = [SettingModule::class, ViewModelModule::class],
    dependencies = [SettingDeps::class]
)
internal interface SettingComponent {

    fun inject(settingFragment: SettingFragment)

    @Component.Builder
    interface Builder {
        fun deps(settingDeps: SettingDeps): Builder
        fun builder(): SettingComponent
    }
}


@Scope
annotation class SettingScope

internal class SettingComponentViewModel : ViewModel() {

    val component = DaggerSettingComponent.builder().deps(SettingDepsProvider.deps).builder()

}

