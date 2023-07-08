package com.template.setting_feature.di.deps

import android.content.Context
import androidx.annotation.RestrictTo
import com.template.api.services.TaskService
import com.template.api.services.YandexAccountService
import com.template.database.AppDatabase
import kotlin.properties.Delegates

interface SettingDeps {

    val database: AppDatabase
    val yandexAccountService: YandexAccountService
    val context: Context
}


interface SettingDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: SettingDeps

    companion object : SettingDepsProvider by SettingDepsStore

}

object SettingDepsStore : SettingDepsProvider {
    override var deps: SettingDeps by Delegates.notNull()
}