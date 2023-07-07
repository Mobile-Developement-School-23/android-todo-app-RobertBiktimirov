package com.template.todoapp.di

import android.content.Context
import com.template.api.services.TodoService
import com.template.database.AppDatabase
import com.template.task_feature.di.deps.TaskDeps
import com.template.todoapp.app.TodoApplication
import com.template.todoapp.di.modules.AppModule
import com.template.todoapp.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent : TaskDeps {

    override val database: AppDatabase
    override val todoService: TodoService
    override val context: Context

    fun inject(mainActivity: MainActivity)
    fun inject(application: TodoApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}