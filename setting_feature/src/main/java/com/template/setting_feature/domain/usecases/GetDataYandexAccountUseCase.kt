package com.template.setting_feature.domain.usecases

import com.template.setting_feature.domain.repository.SettingRepository
import javax.inject.Inject

class GetDataYandexAccountUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
}