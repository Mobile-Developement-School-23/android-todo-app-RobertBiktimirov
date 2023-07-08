package com.template.todoapp.di.modules

import android.content.Context
import com.template.api.RetrofitRepository
import com.template.api.services.TaskService
import com.template.api.services.YandexAccountService
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
    fun provideRetrofitRepository(context: Context): RetrofitRepository =
        RetrofitRepository(context)

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideYandexAccountService(retrofitRepository: RetrofitRepository): YandexAccountService {
        return retrofitRepository.yandexAccountService
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
    fun provideTodoService(retrofitRepository: RetrofitRepository): TaskService =
        retrofitRepository.taskApi

}