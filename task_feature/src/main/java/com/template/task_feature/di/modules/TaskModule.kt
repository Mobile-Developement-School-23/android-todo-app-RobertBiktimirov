package com.template.task_feature.di.modules

import com.template.database.AppDatabase
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.task_feature.data.repository.TodoItemRepositoryImpl
import com.template.task_feature.data.sources.api.ApiSource
import com.template.task_feature.data.sources.api.ApiSourceImpl
import com.template.task_feature.data.sources.database.DatabaseSource
import com.template.task_feature.data.sources.database.DatabaseSourceImpl
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

    @TaskScope
    @Provides
    fun provideRequestDao(appDatabase: AppDatabase): RequestDao {
        return appDatabase.requestDao()
    }

    @Module
    interface BindModule {
        @Binds
        @TaskScope
        fun bindTodoItemRepository(impl: TodoItemRepositoryImpl): TodoItemRepository

        @Binds
        @TaskScope
        fun bindApiSource(impl: ApiSourceImpl): ApiSource

        @Binds
        @TaskScope
        fun bindDatabaseSource(impl: DatabaseSourceImpl): DatabaseSource
    }
}