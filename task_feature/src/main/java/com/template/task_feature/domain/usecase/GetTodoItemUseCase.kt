package com.template.task_feature.domain.usecase

import com.template.common.utli.RepositoryResult
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.repository.TodoItemRepository
import javax.inject.Inject

internal class GetTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    suspend operator fun invoke(id: String): RepositoryResult<TodoItem> {
        return repository.getTodoItem(id)
    }

}