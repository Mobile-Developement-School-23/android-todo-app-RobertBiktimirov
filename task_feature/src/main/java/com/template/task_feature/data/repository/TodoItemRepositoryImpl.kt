package com.template.task_feature.data.repository

import com.template.api.services.TodoService
import com.template.database.dao.TodoDao
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.repository.TodoItemRepository
import com.template.todoapp.data.mappers.toEntity
import com.template.todoapp.data.mappers.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
        return todoDao.saveTodoItem(todoItem.toEntity())
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