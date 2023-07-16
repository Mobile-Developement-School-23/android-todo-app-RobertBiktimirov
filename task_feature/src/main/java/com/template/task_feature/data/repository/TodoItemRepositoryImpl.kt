package com.template.task_feature.data.repository

import android.util.Log
import com.template.common.utli.ApiError
import com.template.common.utli.ApiException
import com.template.common.utli.ApiSuccess
import com.template.common.utli.RepositoryError
import com.template.common.utli.RepositoryException
import com.template.common.utli.RepositoryResult
import com.template.common.utli.RepositorySuccess
import com.template.database.entity.ViewRequest
import com.template.task_feature.data.mappers.toEntity
import com.template.task_feature.data.mappers.toUi
import com.template.task_feature.data.sources.api.ApiSource
import com.template.task_feature.data.sources.database.DatabaseSource
import com.template.task_feature.data.sources.revision.RevisionProvider
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import com.template.task_feature.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TodoItemRepositoryImpl @Inject constructor(
    private val apiSource: ApiSource,
    private val databaseSource: DatabaseSource,
    private val revisionProvider: RevisionProvider
) : TodoItemRepository {

    override fun getTodoList(): Flow<TodoShell> {
        return databaseSource.getListTodoCache()
    }

    override suspend fun firstLoadTodoList(): RepositoryResult<List<TodoItem>> {

        val requestsCache = databaseSource.getRequests()
        val response = apiSource.getListTodoApi()

        Log.d("tokensTest", response.toString())

        if (response is ApiSuccess) {
            revisionProvider.updateRevision(response.data.revision)

            requestsCache.forEach { request ->

                when (request.view) {
                    ViewRequest.UPDATE -> {
                        when (apiSource.editTodoApi(request.todoItemEntity.toUi())) {
                            is ApiSuccess -> {
                                databaseSource.deleteRequest(request)
                            }
                            else -> {}
                        }
                    }
                    ViewRequest.DELETE -> {
                        when (val answer = apiSource.deleteTodoApi(request.todoItemEntity.id)) {
                            is ApiSuccess -> {
                                databaseSource.deleteRequest(request)
                            }
                            is ApiError -> {
                                if (answer.code == 404) {
                                    databaseSource.deleteRequest(request)
                                }
                            }
                            is ApiException -> {}
                        }
                    }
                    ViewRequest.SAVE -> {
                        when (apiSource.saveInApi(request.todoItemEntity.toUi())) {
                            is ApiSuccess -> {
                                databaseSource.deleteRequest(request)
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        return when (val apiResponse = apiSource.getListTodoApi()) {
            is ApiError -> {
                RepositoryError(apiResponse.code, apiResponse.message)
            }
            is ApiException -> {
                RepositoryException(apiResponse.e)
            }
            is ApiSuccess -> {
                databaseSource.saveInCacheTodoList(apiResponse.data.list.toUi().todoItem)
                revisionProvider.updateRevision(apiResponse.data.revision)
                RepositorySuccess(apiResponse.data.list.toUi().todoItem)
            }
        }
    }

    override suspend fun saveTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem> {
        databaseSource.saveInCacheTodoItem(todoItem)

        return when (val apiResponse = apiSource.saveInApi(todoItem)) {
            is ApiError -> {
                databaseSource.saveRequest(ViewRequest.SAVE, todoItem.toEntity())
                RepositoryError(apiResponse.code, apiResponse.message)
            }
            is ApiException -> {
                databaseSource.saveRequest(ViewRequest.SAVE, todoItem.toEntity())
                RepositoryException(apiResponse.e)
            }
            is ApiSuccess -> {
                revisionProvider.updateRevision(apiResponse.data.revision)
                RepositorySuccess(apiResponse.data.todoItem.toUi())
            }
        }
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem> {

        databaseSource.deleteTodoCache(todoItem)

        return when (val apiResponse = apiSource.deleteTodoApi(todoItem.id)) {
            is ApiError -> {
                databaseSource.saveRequest(ViewRequest.DELETE, todoItem.toEntity())
                RepositoryError(apiResponse.code, apiResponse.message)
            }
            is ApiException -> {
                databaseSource.saveRequest(ViewRequest.DELETE, todoItem.toEntity())
                RepositoryException(apiResponse.e)
            }
            is ApiSuccess -> {
                revisionProvider.updateRevision(apiResponse.data.revision)
                RepositorySuccess(apiResponse.data.todoItem.toUi())
            }
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem): RepositoryResult<TodoItem> {
        databaseSource.editTodoCache(todoItem)

        return when (val apiResponse = apiSource.editTodoApi(todoItem)) {
            is ApiError -> {
                databaseSource.saveRequest(ViewRequest.UPDATE, todoItem.toEntity())
                RepositoryError(apiResponse.code, apiResponse.message)
            }
            is ApiException -> {
                databaseSource.saveRequest(ViewRequest.UPDATE, todoItem.toEntity())
                RepositoryException(apiResponse.e)
            }
            is ApiSuccess -> {
                revisionProvider.updateRevision(apiResponse.data.revision)
                RepositorySuccess(apiResponse.data.todoItem.toUi())
            }
        }
    }

    override suspend fun getTodoItem(id: String): RepositoryResult<TodoItem> {
        val cache = databaseSource.getItemTodoCache(id)
        val todoItem = if (cache != null) {
            return RepositorySuccess(cache)
        } else {
            when (val apiResponse = apiSource.getItemTodoApi(id)) {
                is ApiError -> {
                    RepositoryError(apiResponse.code, apiResponse.message)
                }
                is ApiException -> {
                    RepositoryException(apiResponse.e)
                }
                is ApiSuccess -> {
                    revisionProvider.updateRevision(apiResponse.data.revision)
                    RepositorySuccess(apiResponse.data.todoItem.toUi())
                }
            }
        }

        return todoItem
    }
}