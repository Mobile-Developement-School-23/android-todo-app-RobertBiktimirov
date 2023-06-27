package com.template.task_feature.data.repository

import android.util.Log
import com.template.task_feature.data.sources.api.ApiSource
import com.template.task_feature.data.sources.database.DatabaseSource
import com.template.task_feature.data.sources.revision.RevisionProvider
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import com.template.task_feature.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val apiSource: ApiSource,
    private val databaseSource: DatabaseSource,
    private val revisionProvider: RevisionProvider
) : TodoItemRepository {

    private var revision = revisionProvider.spRevision
        set(value) {
            revisionProvider.spRevision = value
            field = value
        }

    override fun getTodoList(): Flow<TodoShell> {
        return flow {

            val databaseResponse = databaseSource.getListTodoCache()
            emit(databaseResponse)

            val apiResponse = apiSource.getListTodoApi() ?: throw RuntimeException()
            databaseSource.saveInCacheTodoList(apiResponse.todoItem)
            emit(apiResponse)
        }
    }

    override suspend fun saveTodoItem(todoItem: TodoItem) {

        if (apiSource.saveInApi(todoItem)) {
            databaseSource.saveInCacheTodoItem(todoItem)
        } else {
            Log.d("error source database", "error save")
        }

    }

    override suspend fun deleteTodoItem(id: String) {

        if (apiSource.deleteTodoApi(id)) {
            databaseSource.deleteTodoCache(id)
        } else {
            Log.d("error source database", "error delete")
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {

        val apiAnswer = apiSource.editTodoApi(todoItem)

        if (apiAnswer != null) {
            databaseSource.editTodoCache(apiAnswer)
        } else {
            Log.d("error source database", "error update")
        }
    }

    override suspend fun getTodoItem(id: String): TodoItem {

        return databaseSource.getItemTodoCache(id)
            ?: apiSource.getItemTodoApi(id)
            ?: throw RuntimeException()
    }
}