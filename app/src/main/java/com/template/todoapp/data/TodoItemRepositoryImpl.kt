package com.template.todoapp.data

import com.template.todoapp.data.database.dao.TodoDao
import com.template.todoapp.data.mappers.toEntity
import com.template.todoapp.data.mappers.toUi
import com.template.todoapp.domain.entity.TodoItem
import com.template.todoapp.domain.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoItemRepository {

    override fun getTodoList(): Flow<List<TodoItem>> {
        return todoDao.getTodoItems().map { it.toUi() }
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