@file:Suppress("UNCHECKED_CAST")

package com.template.task_feature.di.modules.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.template.task_feature.di.modules.viewmodels.ViewModelFactory
import com.template.task_feature.di.modules.viewmodels.ViewModelKey
import com.template.todoapp.ui.main_screen.MainViewModel
import com.template.todoapp.ui.task_screen.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    fun taskViewModel(viewModel: TaskViewModel): ViewModel
}