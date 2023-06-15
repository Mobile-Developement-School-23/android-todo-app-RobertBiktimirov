package com.template.todoapp.domain.usecase

import com.template.todoapp.domain.TodoItem
import com.template.todoapp.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    operator fun invoke(): Flow<List<TodoItem>> = repository.getTodoList()
}