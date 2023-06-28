package com.template.task_feature.data.sources.database

import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import kotlinx.coroutines.flow.Flow

interface DatabaseSource {
    fun getListTodoCache(): Flow<TodoShell>
    suspend fun saveInCacheTodoItem(todoItem: TodoItem)
    suspend fun saveInCacheTodoList(todoItems: List<TodoItem>)
    suspend fun editTodoCache(todoItem: TodoItem)
    suspend fun deleteTodoCache(todoId: String)
    suspend fun getItemTodoCache(todoId: String): TodoItem?
}