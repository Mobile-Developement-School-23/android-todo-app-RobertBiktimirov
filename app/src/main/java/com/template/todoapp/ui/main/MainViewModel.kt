package com.template.todoapp.ui.main

import androidx.lifecycle.ViewModel
import com.template.todoapp.domain.usecases.GetThemeFlowUseCase
import com.template.todoapp.domain.usecases.SaveThemeUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getThemeFlowUseCase: GetThemeFlowUseCase,
    private val setThemeUseCase: SaveThemeUseCase
) : ViewModel() {

    val themeFlow = getThemeFlowUseCase()

    companion object {
        private const val NAME_DATA_TOKEN = "tokenSaveName"
    }
}