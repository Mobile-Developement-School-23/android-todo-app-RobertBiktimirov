package com.template.task_feature.domain.repository

import com.template.task_feature.domain.entity.RepositoryResult
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import kotlinx.coroutines.flow.Flow

internal interface TodoItemRepository {

    fun getTodoList(): Flow<TodoShell>

    suspend fun firstLoadTodoList(): RepositoryResult<List<TodoItem>>

    suspend fun saveTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem>

    suspend fun deleteTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem>

    suspend fun updateTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem>

    suspend fun getTodoItem(id: String): RepositoryResult<TodoItem>

}