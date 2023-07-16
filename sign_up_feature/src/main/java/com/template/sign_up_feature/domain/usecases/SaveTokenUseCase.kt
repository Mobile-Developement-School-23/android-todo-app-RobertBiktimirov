package com.template.sign_up_feature.domain.usecases

import com.template.sign_up_feature.domain.repository.SignRepository
import com.yandex.authsdk.YandexAuthToken
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: SignRepository
){

    suspend operator fun invoke(token: YandexAuthToken): String = repository.saveToken(token)
}