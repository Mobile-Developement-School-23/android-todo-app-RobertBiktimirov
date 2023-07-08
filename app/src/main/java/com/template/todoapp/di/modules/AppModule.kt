package com.template.todoapp.di.modules

import android.content.Context
import com.template.api.RetrofitRepository
import com.template.api.services.TaskService
import com.template.database.AppDatabase
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
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

    @Singleton
    @Provides
    fun provideRequestDao(appDatabase: AppDatabase): RequestDao = appDatabase.requestDao()


    @Provides
    @Singleton
    fun provideTodoServise(context: Context): TaskService = RetrofitRepository(context).taskApi

}