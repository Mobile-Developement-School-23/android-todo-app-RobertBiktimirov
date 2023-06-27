package com.template.task_feature.data.sources.api

import com.template.api.services.TodoService
import com.template.common.utli.runCatchingNonCancellation
import com.template.task_feature.data.mappers.toBody
import com.template.task_feature.data.mappers.toUi
import com.template.task_feature.data.sources.revision.RevisionProvider
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import javax.inject.Inject

class ApiSourceImpl @Inject constructor(
    private val todoService: TodoService,
    private val revisionProvider: RevisionProvider
) : ApiSource {

    override suspend fun getListTodoApi(): TodoShell? {
        val apiResponse = runCatchingNonCancellation {
            todoService.getTodoList()
        }.getOrElse {
            return null
        }

        revisionProvider.spRevision = apiResponse.revision
        return apiResponse.list.toUi()
    }

    override suspend fun saveInApi(todoItem: TodoItem): Boolean {
        runCatchingNonCancellation {
            val answer = todoService.saveTodoItem(todoItem.toBody(), revisionProvider.spRevision)
            if (answer.status == "ok") {
                revisionProvider.spRevision = answer.revision
            }
        }.getOrElse {
            return false
        }

        return true
    }

    override suspend fun editTodoApi(todoItem: TodoItem): TodoItem? {
        runCatchingNonCancellation {
            val apiAnswer = todoService.editTodoItem(
                todoItem.id,
                todoItem.toBody(),
                revisionProvider.spRevision
            )

            revisionProvider.spRevision += 1

            if (apiAnswer.status == "ok") {
                return apiAnswer.todoItem.toUi()
            }
        }.getOrElse {
            return null
        }
        return null
    }

    override suspend fun deleteTodoApi(todoId: String): Boolean {
        runCatchingNonCancellation {
            val apiAnswer = todoService.deleteTodoItem(todoId, revisionProvider.spRevision)
            revisionProvider.spRevision += 1
            if (apiAnswer.todoItem.id == todoId) {
                return true
            }
        }.getOrElse {
            return false
        }

        return false
    }

    override suspend fun getItemTodoApi(id: String): TodoItem? {

        runCatchingNonCancellation {
            val todoItem = todoService.getTodoItem(id)

            if (todoItem.status == "ok") {
                return todoItem.todoItem.toUi()
            }

        }.getOrElse {
            return null
        }
        return null
    }
}