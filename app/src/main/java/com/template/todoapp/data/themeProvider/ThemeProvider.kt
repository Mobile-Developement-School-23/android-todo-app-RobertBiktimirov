package com.template.todoapp.data.themeProvider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.template.todoapp.domain.entity.ThemeEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeProvider @Inject constructor(
    context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = context.getString(com.template.resourses_module.R.string.name_datastore_theme)
    )

    private val dataStore = context.dataStore

    companion object {
        private val themeModKey = stringPreferencesKey("THEME_KEY_MODE")
        private const val DARK_THEME = "darkTheme"
        private const val DAY_THEME = "dayTheme"
        private const val SYSTEM_THEME = "systemTheme"
    }


    suspend fun setTheme(themeEnum: ThemeEnum) {

        dataStore.edit {
            it[themeModKey] = when (themeEnum) {
                ThemeEnum.DARK -> {
                    DARK_THEME
                }

                ThemeEnum.DAY -> {
                    DAY_THEME
                }

                ThemeEnum.SYSTEM -> {
                    SYSTEM_THEME
                }
            }
        }
    }


    fun getTheme(): Flow<ThemeEnum> =
        dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { pref ->
                when (pref[themeModKey]) {
                    DARK_THEME -> {
                        ThemeEnum.DARK
                    }
                    DAY_THEME -> {
                        ThemeEnum.DAY
                    }
                    SYSTEM_THEME -> {
                        ThemeEnum.SYSTEM
                    }
                    else -> {
                        throw RuntimeException()
                    }
                }
            }
}