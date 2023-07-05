@file:Suppress("UNCHECKED_CAST")

package com.template.task_feature.di.modules.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.template.task_feature.ui.task_list_screen.TaskListViewModel
import com.template.task_feature.ui.task_screen.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    fun mainViewModel(viewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    fun taskViewModel(viewModel: TaskViewModel): ViewModel
}