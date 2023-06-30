package com.template.task_feature.data.sources.api

import com.template.api.entity.TodoListResponse
import com.template.api.entity.TodoResponse
import com.template.api.services.TodoService
import com.template.common.utli.ApiResult
import com.template.common.utli.handleApi
import com.template.task_feature.data.mappers.toBody
import com.template.task_feature.data.sources.revision.RevisionProvider
import com.template.task_feature.domain.entity.TodoItem
import javax.inject.Inject

class ApiSourceImpl @Inject constructor(
    private val todoService: TodoService,
    private val revisionProvider: RevisionProvider
) : ApiSource {

    override suspend fun getListTodoApi(): ApiResult<TodoListResponse> {
        return handleApi { todoService.getTodoList() }
    }

    override suspend fun saveInApi(todoItem: TodoItem): ApiResult<TodoResponse> {
        return handleApi {
            todoService.saveTodoItem(
                todoItem.toBody(),
                revisionProvider.getRevision()
            )
        }
    }

    override suspend fun editTodoApi(todoItem: TodoItem): ApiResult<TodoResponse> {
        return handleApi {
            todoService.editTodoItem(
                todoItem.id,
                todoItem.toBody(),
                revisionProvider.getRevision()
            )
        }
    }

    override suspend fun deleteTodoApi(todoId: String): ApiResult<TodoResponse> {
        return handleApi {
            todoService.deleteTodoItem(todoId, revisionProvider.getRevision())
        }
    }

    override suspend fun getItemTodoApi(id: String): ApiResult<TodoResponse> {
        return handleApi {
            todoService.getTodoItem(id)
        }
    }
}