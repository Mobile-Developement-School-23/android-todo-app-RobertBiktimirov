package com.template.todoapp.domain.usecase

import com.template.todoapp.domain.entity.TodoItem
import com.template.todoapp.domain.repository.TodoItemRepository
import javax.inject.Inject

class SaveTodoItemUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {
    suspend operator fun invoke(todoItem: TodoItem) {
        return repository.saveTodoItem(todoItem)
    }
}