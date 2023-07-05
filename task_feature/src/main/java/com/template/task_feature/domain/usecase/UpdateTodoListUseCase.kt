package com.template.task_feature.domain.usecase

import com.template.task_feature.domain.entity.RepositoryResult
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.repository.TodoItemRepository
import javax.inject.Inject

internal class UpdateTodoListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(todoItem: TodoItem): RepositoryResult<TodoItem> {
        return repository.updateTodoItem(todoItem)
    }
}