package com.template.todoapp.domain.usecase

import com.template.todoapp.domain.repository.TodoItemRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoItemRepository
) {

    suspend operator fun invoke(id: String) {
        return repository.deleteTodoItem(id)
    }
}