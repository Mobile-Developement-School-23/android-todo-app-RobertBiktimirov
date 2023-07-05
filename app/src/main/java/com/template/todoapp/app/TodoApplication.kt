package com.template.todoapp.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.template.task_feature.di.deps.TaskDepsStore
import com.template.todoapp.di.AppComponent
import com.template.todoapp.di.DaggerAppComponent
import com.template.todoapp.ui.services.factory.CreateWorkerFactory
import javax.inject.Inject

class TodoApplication : Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = requireNotNull(_appComponent) {
            "AppComponent must be not null"
        }

    @Inject
    lateinit var createWorkerFactory: CreateWorkerFactory

    private val configurationWorker by lazy {
        Configuration.Builder()
            .setWorkerFactory(createWorkerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        appComponent.inject(this)


        TaskDepsStore.deps = appComponent

        WorkManager.initialize(this, configurationWorker)
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is TodoApplication -> appComponent
        else -> (applicationContext as TodoApplication).appComponent
    }