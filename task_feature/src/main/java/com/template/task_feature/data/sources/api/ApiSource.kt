package com.template.task_feature.data.sources.api

import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell

interface ApiSource {

    suspend fun getListTodoApi(): TodoShell?
    suspend fun saveInApi(todoItem: TodoItem): Boolean
    suspend fun editTodoApi(todoItem: TodoItem): TodoItem?
    suspend fun deleteTodoApi(todoId: String): Boolean
    suspend fun getItemTodoApi(id: String): TodoItem?
}