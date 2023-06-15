@file:Suppress("UNCHECKED_CAST")

package com.template.todoapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.template.todoapp.di.viewmodels.ViewModelFactory
import com.template.todoapp.di.viewmodels.ViewModelKey
import com.template.todoapp.ui.main_screen.MainViewModel
import com.template.todoapp.ui.task_screen.TaskViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

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