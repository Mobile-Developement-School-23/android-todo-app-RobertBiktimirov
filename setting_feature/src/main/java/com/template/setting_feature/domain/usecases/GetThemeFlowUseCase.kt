package com.template.setting_feature.domain.usecases

import com.template.setting_feature.domain.repository.SettingRepository
import javax.inject.Inject

class GetThemeFlowUseCase @Inject constructor(
    private val repository: SettingRepository
) {

    operator fun invoke() = repository.getTheme()

}