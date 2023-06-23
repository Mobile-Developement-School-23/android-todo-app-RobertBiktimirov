package com.template.todoapp.domain.usecase

import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    operator fun invoke(): Flow<List<TodoItem>> = repository.getTodoList()
}