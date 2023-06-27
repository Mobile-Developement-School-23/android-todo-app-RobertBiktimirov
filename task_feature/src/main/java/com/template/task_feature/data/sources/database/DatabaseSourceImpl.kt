package com.template.task_feature.data.sources.database

import android.util.Log
import com.template.common.utli.runCatchingNonCancellation
import com.template.database.dao.TodoDao
import com.template.task_feature.data.mappers.toDto
import com.template.task_feature.data.mappers.toEntity
import com.template.task_feature.data.mappers.toUi
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import javax.inject.Inject

class DatabaseSourceImpl @Inject constructor(
    private val todoDao: TodoDao
) : DatabaseSource {

    override suspend fun getListTodoCache(): TodoShell {
        return todoDao.getTodoItems().toUi()
    }

    override suspend fun saveInCacheTodoItem(todoItem: TodoItem) {
        runCatchingNonCancellation {
            todoDao.saveTodoItem(todoItem.toEntity())
        }.getOrElse {
            Log.d("error source database", it.message.toString())
        }

    }

    override suspend fun saveInCacheTodoList(todoItems: List<TodoItem>) {
        runCatchingNonCancellation {
            val items = todoItems.toEntity()
            todoDao.deleteAll()
            todoDao.saveTodoList(items)
        }.getOrElse {
            Log.d("error source database", it.message.toString())
        }
    }

    override suspend fun editTodoCache(todoItem: TodoItem) {
        runCatchingNonCancellation {
            todoDao.updateTodoItem(
                todoItem.text,
                todoItem.importance.toDto(),
                todoItem.deadline,
                todoItem.isCompleted,
                todoItem.dateOfEditing,
                todoItem.id
            )
        }
    }

    override suspend fun deleteTodoCache(todoId: String) {
        runCatchingNonCancellation {
            todoDao.deleteTodoItem(todoId)
        }.getOrElse {
            Log.d("error source database", it.message.toString())
        }
    }

    override suspend fun getItemTodoCache(todoId: String): TodoItem? {
        val todoItem = runCatchingNonCancellation {
            todoDao.getTodoItem(todoId)
        }.getOrNull()

        return todoItem?.toUi()
    }
}