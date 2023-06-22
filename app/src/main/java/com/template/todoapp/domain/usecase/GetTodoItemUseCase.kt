package com.template.todoapp.domain.usecase

import com.template.todoapp.domain.entity.TodoItem
import com.template.todoapp.domain.repository.TodoItemRepository
import javax.inject.Inject

class GetTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    suspend operator fun invoke(id: String): TodoItem {
        return repository.getTodoItem(id)
    }

}