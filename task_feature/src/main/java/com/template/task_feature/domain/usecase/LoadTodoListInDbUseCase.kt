package com.template.task_feature.domain.usecase

import com.template.task_feature.domain.repository.TodoItemRepository
import javax.inject.Inject

internal class LoadTodoListInDbUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke() = repository.firstLoadTodoList()
}