package com.template.task_feature.di.modules

import androidx.lifecycle.LifecycleOwner
import com.template.task_feature.databinding.FragmentMainBinding
import com.template.task_feature.databinding.ItemTaskListBinding
import com.template.task_feature.ui.task_list_screen.TaskListFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class ViewScope


@Qualifier
annotation class ViewLifecycleOwner

@Subcomponent
@ViewScope
interface TaskListFragmentViewsComponent {

    fun boot(): TaskListViewBootstrapper

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun binding(binding: FragmentMainBinding): Builder

        @ViewLifecycleOwner
        @BindsInstance
        fun binding(lifecycleOwner: LifecycleOwner): Builder

        fun build(): TaskListFragmentViewsComponent
    }
}