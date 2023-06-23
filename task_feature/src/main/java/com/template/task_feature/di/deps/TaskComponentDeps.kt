package com.template.task_feature.di.deps

import androidx.annotation.RestrictTo
import com.template.database.AppDatabase
import kotlin.properties.Delegates.notNull

interface TaskDeps {

    val database: AppDatabase
}


interface TaskDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: TaskDeps

    companion object : TaskDepsProvider by TaskDepsStore

}

object TaskDepsStore : TaskDepsProvider {
    override var deps: TaskDeps by notNull()
}