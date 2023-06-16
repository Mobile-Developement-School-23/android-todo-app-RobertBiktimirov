package com.template.todoapp.data

import com.template.todoapp.data.database.AppDatabase
import com.template.todoapp.data.database.dao.TodoDao
import com.template.todoapp.data.mappers.toEntity
import com.template.todoapp.data.mappers.toUi
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class TodoItemRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoItemRepository {

    override fun getTodoList(): Flow<List<TodoItem>> {
        return todoDao.getTodoItems().map { it.toUi() }
    }

    override suspend fun saveTodoItem(todoItem: TodoItem) {
        return todoDao.saveTodoItem(todoItem.toEntity())
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        return todoDao.deleteTodoItem(todoItem.toEntity().id)
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
}