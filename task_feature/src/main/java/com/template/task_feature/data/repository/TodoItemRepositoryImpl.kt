package com.template.task_feature.data.repository

import android.util.Log
import com.template.api.services.TodoService
import com.template.database.dao.TodoDao
import com.template.task_feature.data.mappers.toBody
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.repository.TodoItemRepository
import com.template.task_feature.data.mappers.toEntity
import com.template.task_feature.data.mappers.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val todoService: TodoService
) : TodoItemRepository {

    override fun getTodoList(): Flow<List<TodoItem>> {
        return flow {
            emit(todoService.getTodoList().list.toUi())
        }
    }

    override suspend fun saveTodoItem(todoItem: TodoItem) {
        val apiResponse = todoService.saveTodoItem(todoItem.toBody(), 0)
        Log.d("api test save todoItem", apiResponse.toString())
    }

    override suspend fun deleteTodoItem(id: String) {
        return todoDao.deleteTodoItem(id)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        val entity = todoItem.toEntity()
        return todoDao.updateTodoItem(
            entity.text,
            entity.importance,
            entity.deadline,
            if (entity.flag) 1 else 0,
            entity.dateOfCreating,
            entity.id
        )
    }

    override suspend fun getTodoItem(id: String): TodoItem {
        return todoDao.getTodoItem(id).toUi()
    }
}