package com.template.todoapp.domain.repository

import com.template.todoapp.domain.entity.ThemeEnum
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun initTheme()

    fun getTheme(): Flow<ThemeEnum>

    suspend fun setTheme(themeEnum: ThemeEnum)

}