package com.template.todoapp.ui.main

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.todoapp.domain.entity.ThemeEnum
import com.template.todoapp.domain.usecases.GetThemeFlowUseCase
import com.template.todoapp.domain.usecases.SaveThemeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getThemeFlowUseCase: GetThemeFlowUseCase,
    private val setThemeUseCase: SaveThemeUseCase
) : ViewModel() {

    val themeFlow = getThemeFlowUseCase()

    fun setTheme(themeEnum: ThemeEnum) {
        viewModelScope.launch {
            setThemeUseCase(themeEnum)
        }
    }


    companion object {
        private const val NAME_DATA_TOKEN = "tokenSaveName"
    }
}