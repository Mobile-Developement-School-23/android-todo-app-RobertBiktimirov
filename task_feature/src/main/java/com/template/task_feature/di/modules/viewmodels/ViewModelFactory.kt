package com.template.task_feature.di.modules.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.template.task_feature.di.TaskScope
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

//@TaskScope
//class ViewModelFactory @Inject constructor(
//    private val viewModels: MutableMap<Class<out ViewModel>,
//            @JvmSuppressWildcards Provider<ViewModel>>
//) : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T =
//        viewModels[modelClass]?.get() as T
//
//}