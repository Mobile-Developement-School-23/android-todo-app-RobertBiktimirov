package com.template.todoapp.data

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.template.api.entity.TodoBody
import com.template.api.entity.TodoItemApi
import com.template.api.entity.TodoResponse
import com.template.api.services.TaskService
import com.template.common.theme.ThemeEnumCommon
import com.template.common.theme.ThemeProvider
import com.template.common.utli.ApiError
import com.template.common.utli.ApiException
import com.template.common.utli.ApiSuccess
import com.template.common.utli.handleApi
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.database.entity.TodoItemEntity
import com.template.database.entity.ViewRequest
import com.template.task_feature.data.mappers.toDto
import com.template.task_feature.data.mappers.toImportance
import com.template.task_feature.data.mappers.toUi
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem
import com.template.todoapp.domain.entity.ThemeEnum
import com.template.todoapp.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Provider
import com.template.resourses_module.R as resR

class MainRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val requestDao: RequestDao,
    private val todoService: Provider<TaskService>,
    private val context: Context,
    private val themeProvider: ThemeProvider
) : MainRepository {

    private val revisionSharedPreferences =
        context.getSharedPreferences(
            context.getString(resR.string.name_revision_shared_preference),
            Context.MODE_PRIVATE
        )

    private val themeSharedPrefernces =
        context.getSharedPreferences(
            context.getString(resR.string.key_sp_theme),
            Context.MODE_PRIVATE
        )

    suspend fun loadDataInDb() {

        val apiResponse = todoService.get().getTodoList()
        val body = apiResponse.body()
        if (body != null && body.status == "ok") {
            todoDao.deleteAll()
            todoDao.saveTodoList(body.list.toEntity())
            saveRevision(body.revision)
        }
    }

    suspend fun loadNewDataFromDb() {
        val requests = requestDao.getRequest()
        val response = todoService.get().getTodoList().body()
        var revision = response?.revision
            ?: revisionSharedPreferences.getInt(context.getString(resR.string.key_sp_revision), 0)

        Log.d("nullTokenTest", "loadNewDataFromDb")
        if (response != null) {

            requests.forEach { request ->
                when (request.view) {
                    ViewRequest.UPDATE -> {
                        revision = loadItemRequest {
                            todoService.get().editTodoItem(
                                request.todoItemEntity.id,
                                request.todoItemEntity.toUi().toBody(),
                                revision
                            )
                        } ?: revision
                    }

                    ViewRequest.DELETE -> {
                        revision = loadItemRequest {
                            todoService.get().deleteTodoItem(request.todoItemEntity.id, revision)
                        } ?: revision
                    }

                    ViewRequest.SAVE -> {
                        revision = loadItemRequest {
                            todoService.get().saveTodoItem(
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
        revisionSharedPreferences.edit()
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

    override suspend fun initTheme() {

        val theme =
            themeSharedPrefernces.getString(context.getString(resR.string.key_sp_theme), null)

        theme?.let {
            when(it) {

                context.getString(resR.string.key_dark_theme_application) -> {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                }
                context.getString(resR.string.key_light_theme_application) -> {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_UNSPECIFIED)
                }
            }
        }
    }

    override fun getTheme(): Flow<ThemeEnum> {
        return themeProvider.getTheme().map {
            when(it) {
                ThemeEnumCommon.DARK -> {ThemeEnum.DARK}
                ThemeEnumCommon.DAY -> {ThemeEnum.DAY}
                ThemeEnumCommon.SYSTEM -> {ThemeEnum.SYSTEM}
            }
        }
    }

    override suspend fun setTheme(themeEnum: ThemeEnum) {
        themeProvider.setTheme(
            when(themeEnum) {
                ThemeEnum.DARK -> {ThemeEnumCommon.DARK}
                ThemeEnum.DAY -> {ThemeEnumCommon.DAY}
                ThemeEnum.SYSTEM -> {ThemeEnumCommon.SYSTEM}
            }
        )
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