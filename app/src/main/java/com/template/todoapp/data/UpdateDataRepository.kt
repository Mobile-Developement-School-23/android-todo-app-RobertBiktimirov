package com.template.todoapp.data

import android.content.Context
import com.template.api.entity.TodoItemApi
import com.template.api.services.TodoService
import com.template.database.dao.TodoDao
import com.template.database.entity.TodoItemEntity
import com.template.task_feature.data.mappers.toDto
import com.template.task_feature.data.mappers.toImportance
import javax.inject.Inject
import com.template.resourses_module.R as resR

class UpdateDataRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val todoService: TodoService,
    private val context: Context
) {

    private val sharedPreferences =
        context.getSharedPreferences(
            context.getString(com.template.resourses_module.R.string.name_revision_shared_preference),
            Context.MODE_PRIVATE
        )

    suspend fun loadDataInDb() {

        val apiResponse = todoService.getTodoList()
        if (apiResponse.status == "ok") {
            todoDao.deleteAll()
            todoDao.saveTodoList(apiResponse.list.toEntity())
        }
    }


    private fun getRevision() =
        sharedPreferences.getInt(context.getString(resR.string.key_sp_revision), -1)


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

}