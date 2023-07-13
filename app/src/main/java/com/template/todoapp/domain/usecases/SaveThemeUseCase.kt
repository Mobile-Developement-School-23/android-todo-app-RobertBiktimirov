package com.template.todoapp.domain.usecases

import com.template.todoapp.domain.entity.ThemeEnum
import com.template.todoapp.domain.repository.MainRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val repository: MainRepository
) {

    suspend operator fun invoke(themeEnum: ThemeEnum) {
        repository.setTheme(themeEnum)
    }

}