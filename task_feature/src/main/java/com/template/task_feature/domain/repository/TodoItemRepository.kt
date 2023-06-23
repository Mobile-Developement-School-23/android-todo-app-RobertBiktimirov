package com.template.task_feature.domain.repository

import com.template.task_feature.domain.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {

    fun getTodoList(): Flow<List<TodoItem>>

    suspend fun saveTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(id: String)

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(id: String): TodoItem

}