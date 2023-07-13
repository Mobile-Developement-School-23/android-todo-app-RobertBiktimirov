package com.template.common.theme

import kotlinx.coroutines.flow.Flow

interface ThemeProvider {

    fun getTheme(): Flow<ThemeEnumCommon>

    suspend fun setTheme(themeEnum: ThemeEnumCommon)
}


enum class ThemeEnumCommon {
    DARK,
    DAY,
    SYSTEM
}