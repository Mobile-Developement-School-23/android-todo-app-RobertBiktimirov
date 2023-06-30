package com.template.task_feature.data.sources.api

import com.template.api.entity.TodoListResponse
import com.template.api.entity.TodoResponse
import com.template.common.utli.ApiResult
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell

interface ApiSource {

    suspend fun getListTodoApi(): ApiResult<TodoListResponse>
    suspend fun saveInApi(todoItem: TodoItem): ApiResult<TodoResponse>
    suspend fun editTodoApi(todoItem: TodoItem): ApiResult<TodoResponse>
    suspend fun deleteTodoApi(todoId: String): ApiResult<TodoResponse>
    suspend fun getItemTodoApi(id: String): ApiResult<TodoResponse>
}