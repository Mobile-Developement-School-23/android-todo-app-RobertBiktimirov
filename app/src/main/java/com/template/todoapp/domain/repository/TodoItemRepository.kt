package com.template.todoapp.domain.repository

import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {

    fun getTodoList(): Flow<List<TodoItem>>

    suspend fun saveTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun updateTodoItem(todoItem: TodoItem)

}