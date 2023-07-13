package com.template.todoapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.template.api.services.TaskService
import com.template.api.services.YandexAccountService
import com.template.common.theme.ThemeProvider
import com.template.common.theme.ThemeProviderImpl
import com.template.database.AppDatabase
import com.template.setting_feature.di.deps.SettingDeps
import com.template.task_feature.di.deps.TaskDeps
import com.template.todoapp.app.TodoApplication
import com.template.todoapp.di.modules.AppModule
import com.template.todoapp.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent : TaskDeps, SettingDeps {

    override val database: AppDatabase
    override val todoService: TaskService
    override val context: Context
    override val yandexAccountService: YandexAccountService
    override val themeProvider: ThemeProvider

    fun inject(mainActivity: MainActivity)
    fun inject(application: TodoApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}