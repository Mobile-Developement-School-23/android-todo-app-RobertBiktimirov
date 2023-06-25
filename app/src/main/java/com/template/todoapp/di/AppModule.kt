package com.template.todoapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.template.api.RetrofitRepository
import com.template.api.services.TodoService
import com.template.database.AppDatabase
import com.template.database.dao.TodoDao
import com.template.todoapp.R as mainR
import com.template.resourses_module.R as resR
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
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(resR.string.name_shared_preference),
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideTodoServise(context: Context): TodoService = RetrofitRepository(context).api

}