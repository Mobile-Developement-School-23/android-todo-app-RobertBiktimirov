package com.template.setting_feature.domain.usecases

import com.template.setting_feature.domain.repository.SettingRepository
import com.template.setting_feature.ui.models.ThemeModel
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val repository: SettingRepository
) {

    suspend operator fun invoke(themeModel: ThemeModel) = repository.saveTheme(themeModel)

}