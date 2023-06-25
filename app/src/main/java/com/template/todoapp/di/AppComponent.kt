package com.template.todoapp.di

import android.content.Context
import com.template.api.services.TodoService
import com.template.database.AppDatabase
import com.template.task_feature.di.deps.TaskDeps
import com.template.todoapp.ui.MainActivity
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

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}