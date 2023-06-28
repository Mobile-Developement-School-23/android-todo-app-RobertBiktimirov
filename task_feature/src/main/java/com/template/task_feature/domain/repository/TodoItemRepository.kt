package com.template.task_feature.domain.repository

import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {

    fun getTodoList(): Flow<TodoShell>

    suspend fun firstLoadTodoList()

    suspend fun saveTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(id: String)

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(id: String): TodoItem

}