package com.template.sign_up_feature.domain.usecases

import com.template.sign_up_feature.domain.repository.SignRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: SignRepository
){

    suspend operator fun invoke(token: String) = repository.saveToken(token)
}