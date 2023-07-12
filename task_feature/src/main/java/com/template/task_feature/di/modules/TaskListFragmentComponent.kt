package com.template.task_feature.di.modules

import com.template.task_feature.ui.task_list_screen.TaskListFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class FragmentScope

@Subcomponent
@FragmentScope
interface TaskListFragmentComponent {

    fun viewsComponentBuilder(): TaskListFragmentViewsComponent.Builder

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun fragment(fragment: TaskListFragment): Builder

        fun build(): TaskListFragmentComponent
    }
}