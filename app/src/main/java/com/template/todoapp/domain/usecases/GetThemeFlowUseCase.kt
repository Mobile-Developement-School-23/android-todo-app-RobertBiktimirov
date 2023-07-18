package com.template.todoapp.domain.usecases

import com.template.todoapp.domain.repository.MainRepository
import javax.inject.Inject

class GetThemeFlowUseCase @Inject constructor(
    private val repository: MainRepository
){

    operator fun invoke() = repository.getTheme()

}