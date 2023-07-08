package com.template.task_feature.di.deps

import android.content.Context
import androidx.annotation.RestrictTo
import com.template.api.services.TaskService
import com.template.database.AppDatabase
import kotlin.properties.Delegates.notNull

interface TaskDeps {

    val database: AppDatabase
    val todoService: TaskService
    val context: Context
}


interface TaskDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: TaskDeps

    companion object : TaskDepsProvider by TaskDepsStore

}

object TaskDepsStore : TaskDepsProvider {
    override var deps: TaskDeps by notNull()
}