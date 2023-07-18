package com.template.common.theme

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.template.resourses_module.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeProviderImpl(context: Context) : ThemeProvider {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = context.getString(R.string.name_theme_shared_preference)
    )

    private val dataStore = context.dataStore

    companion object {
        private val themeModKey = stringPreferencesKey("THEME_KEY_MODE")
        private const val DARK_THEME = "darkTheme"
        private const val DAY_THEME = "dayTheme"
        private const val SYSTEM_THEME = "systemTheme"
    }


    override suspend fun setTheme(themeEnum: ThemeEnumCommon) {

        dataStore.edit {
            it[themeModKey] = when (themeEnum) {
                ThemeEnumCommon.DARK -> {
                    DARK_THEME
                }

                ThemeEnumCommon.DAY -> {
                    DAY_THEME
                }

                ThemeEnumCommon.SYSTEM -> {
                    SYSTEM_THEME
                }
            }
        }
    }


    override fun getTheme(): Flow<ThemeEnumCommon> =
        dataStore.data
            .map { pref ->
                when (pref[themeModKey]) {
                    DARK_THEME -> {
                        ThemeEnumCommon.DARK
                    }

                    DAY_THEME -> {
                        ThemeEnumCommon.DAY
                    }

                    SYSTEM_THEME -> {
                        ThemeEnumCommon.SYSTEM
                    }

                    else -> {
                        ThemeEnumCommon.SYSTEM
                    }
                }
            }
}