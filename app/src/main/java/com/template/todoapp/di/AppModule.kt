package com.template.todoapp.di

import android.content.Context
import com.template.todoapp.data.TodoItemRepositoryImpl
import com.template.todoapp.data.database.AppDatabase
import com.template.todoapp.data.database.dao.TodoDao
import com.template.todoapp.domain.repository.TodoItemRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule.BindModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideTodoDao(appDatabase: AppDatabase): TodoDao {
        return appDatabase.todoDao()
    }

    @Module
    interface BindModule {

        @Binds
        fun bindTodoItemRepository(impl: TodoItemRepositoryImpl): TodoItemRepository
    }

}