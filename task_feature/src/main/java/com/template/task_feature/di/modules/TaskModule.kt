package com.template.task_feature.di.modules

import com.template.database.AppDatabase
import com.template.database.dao.TodoDao
import com.template.task_feature.data.repository.TodoItemRepositoryImpl
import com.template.task_feature.di.TaskScope
import com.template.task_feature.domain.repository.TodoItemRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [TaskModule.BindModule::class])
class TaskModule {

    @TaskScope
    @Provides
    fun provideTodoDao(appDatabase: AppDatabase): TodoDao {
        return appDatabase.todoDao()
    }

    @Module
    interface BindModule {
        @Binds
        @TaskScope
        fun bindTodoItemRepository(impl: TodoItemRepositoryImpl): TodoItemRepository
    }
}