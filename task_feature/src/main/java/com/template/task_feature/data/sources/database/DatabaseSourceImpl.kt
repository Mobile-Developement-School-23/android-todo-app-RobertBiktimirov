package com.template.task_feature.data.sources.database

import android.util.Log
import com.template.common.utli.runCatchingNonCancellation
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.database.entity.RequestDto
import com.template.database.entity.TodoItemEntity
import com.template.database.entity.ViewRequest
import com.template.task_feature.data.mappers.toDto
import com.template.task_feature.data.mappers.toEntity
import com.template.task_feature.data.mappers.toUi
import com.template.task_feature.data.setup_notification.AlarmScheduler
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class DatabaseSourceImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val requestDao: RequestDao,
    private val alarmScheduler: AlarmScheduler
) : DatabaseSource {

    override fun getListTodoCache(): Flow<TodoShell> {
        return todoDao.getTodoItemsFlow()
            .transform { listTodoItem ->
                emit(listTodoItem.sortedBy { todoItem -> todoItem.dateOfCreating }.toSet().toList())
            }
            .map { it.toUi() }
    }

    override suspend fun saveInCacheTodoItem(todoItem: TodoItem) {
        runCatchingNonCancellation {
            todoDao.saveTodoItem(todoItem.toEntity())
            alarmScheduler.schedule(todoItem)
        }.getOrElse {
            Log.d("error source database", it.message.toString())
        }

    }

    override suspend fun saveInCacheTodoList(todoItems: List<TodoItem>) {
        runCatchingNonCancellation {
            val items = todoItems.toEntity()
            todoDao.deleteAll()
            todoDao.saveTodoList(items)

            todoItems.forEach {
                alarmScheduler.schedule(it)
            }
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

            alarmScheduler.schedule(todoItem)
        }
    }

    override suspend fun deleteTodoCache(todoItem: TodoItem) {
        runCatchingNonCancellation {
            todoDao.deleteTodoItem(todoItem.id)

            alarmScheduler.cancel(todoItem)
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

    override suspend fun saveRequest(viewRequest: ViewRequest, todoItem: TodoItemEntity) {
        requestDao.saveRequest(
            RequestDto(view = viewRequest, todoItemEntity = todoItem, keyId = todoItem.id)
        )
    }

    override suspend fun getRequests(): List<RequestDto> {
        return requestDao.getRequest()
    }

    override suspend fun deleteRequest(requestDto: RequestDto) {
        requestDao.deleteRequest(requestDto)
    }
}