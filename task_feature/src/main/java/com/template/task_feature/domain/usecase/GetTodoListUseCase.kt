package com.template.task_feature.domain.usecase

import com.template.task_feature.domain.entity.TodoShell
import com.template.task_feature.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTodoListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    operator fun invoke(): Flow<TodoShell> = repository.getTodoList()
}