package com.template.todoapp.app

import android.app.Application
import android.content.Context
import com.template.api.ApiTokenProvider
import com.template.task_feature.di.deps.TaskDepsStore
import com.template.todoapp.di.AppComponent
import com.template.todoapp.di.DaggerAppComponent

class TodoApplication : Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = requireNotNull(_appComponent) {
            "AppComponent must be not null"
        }

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()


        TaskDepsStore.deps = appComponent

    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is TodoApplication -> appComponent
        else -> (applicationContext as TodoApplication).appComponent
    }