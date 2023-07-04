package com.template.task_feature.di

import androidx.lifecycle.ViewModel
import com.template.task_feature.di.deps.TaskDeps
import com.template.task_feature.di.deps.TaskDepsProvider
import com.template.task_feature.di.modules.TaskModule
import com.template.task_feature.di.modules.viewmodels.ViewModelModule
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import com.template.task_feature.ui.task_screen.TaskFragment
import dagger.Component
import javax.inject.Scope

@TaskScope
@Component(modules = [TaskModule::class, ViewModelModule::class], dependencies = [TaskDeps::class])
internal interface TaskComponent {

    fun inject(taskListFragment: TaskListFragment)
    fun inject(taskFragment: TaskFragment)

    @Component.Builder
    interface Builder {
        fun deps(taskDeps: TaskDeps): Builder
        fun build(): TaskComponent
    }
}


@Scope
annotation class TaskScope

internal class TaskComponentViewModel : ViewModel() {

    val component = DaggerTaskComponent.builder().deps(TaskDepsProvider.deps).build()

}



