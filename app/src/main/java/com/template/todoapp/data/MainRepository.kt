package com.template.todoapp.data

import android.content.Context
import com.template.api.entity.TodoBody
import com.template.api.entity.TodoItemApi
import com.template.api.entity.TodoResponse
import com.template.api.services.TaskService
import com.template.common.utli.*
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.database.entity.TodoItemEntity
import com.template.database.entity.ViewRequest
import com.template.task_feature.data.mappers.*
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem
import retrofit2.Response
import javax.inject.Inject
import com.template.resourses_module.R as resR

class MainRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val requestDao: RequestDao,
    private val todoService: TaskService,
    private val context: Context
) {

    private val sharedPreferences =
        context.getSharedPreferences(
            context.getString(resR.string.name_revision_shared_preference),
            Context.MODE_PRIVATE
        )

    suspend fun loadDataInDb() {

        val apiResponse = todoService.getTodoList()
        val body = apiResponse.body()
        if (body != null && body.status == "ok") {
            todoDao.deleteAll()
            todoDao.saveTodoList(body.list.toEntity())
            saveRevision(body.revision)
        }
    }


    suspend fun loadNewDataFromDb() {
        val requests = requestDao.getRequest()
        val response = todoService.getTodoList().body()
        var revision = response?.revision
            ?: sharedPreferences.getInt(context.getString(resR.string.key_sp_revision), 0)

        if (response != null) {

            requests.forEach {request ->
                when (request.view) {
                    ViewRequest.UPDATE -> {
                        revision = loadItemRequest {
                            todoService.editTodoItem(
                                request.todoItemEntity.id,
                                request.todoItemEntity.toUi().toBody(),
                                revision
                            )
                        } ?: revision
                    }
                    ViewRequest.DELETE -> {
                        revision = loadItemRequest {
                            todoService.deleteTodoItem(request.todoItemEntity.id, revision)
                        } ?: revision
                    }
                    ViewRequest.SAVE -> {
                        revision = loadItemRequest {
                            todoService.saveTodoItem(
                                request.todoItemEntity.toUi().toBody(),
                                revision
                            )
                        } ?: revision
                    }
                }
                requestDao.deleteRequest(request)
            }
        }

        saveRevision(revision)
    }

    private fun saveRevision(revision: Int) {
        sharedPreferences.edit()
            .putInt(context.getString(resR.string.key_sp_revision), revision)
            .apply()
    }


    private suspend fun loadItemRequest(job: (suspend () -> Response<TodoResponse>)): Int? {

        return when (val apiResult = handleApi { job() }) {
            is ApiError -> {
                null
            }
            is ApiException -> {
                null
            }
            is ApiSuccess -> {
                apiResult.data.revision
            }
        }
    }


    private fun TodoItemApi.toEntity() = TodoItemEntity(
        id = id,
        text = text,
        importance = importance.toImportance().toDto(),
        deadline = deadline,
        flag = done,
        color = color,
        dateOfCreating = createdAt,
        dateOfEditing = changedAt
    )

    private fun List<TodoItemApi>.toEntity() = this.map { it.toEntity() }

    private fun Importance.toBody(): String = when (this) {
        Importance.LOW -> "low"
        Importance.REGULAR -> "basic"
        Importance.URGENT -> "important"
    }


    private fun TodoItem.toBody(): TodoBody {
        val todoItemApi = TodoItemApi(
            id = id,
            text = text,
            importance = importance.toBody(),
            deadline = deadline,
            done = isCompleted,
            color = color,
            createdAt = dateOfCreating,
            changedAt = dateOfEditing ?: dateOfCreating,
            lastUpdateBy = "cf1"
        )

        return TodoBody(todoItemApi)
    }

}