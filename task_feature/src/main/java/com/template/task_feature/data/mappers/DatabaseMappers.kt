package com.template.task_feature.data.mappers

import com.template.api.entity.TodoBody
import com.template.api.entity.TodoItemApi
import com.template.database.entity.ImportanceDto
import com.template.database.entity.TodoItemEntity
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem
import com.template.task_feature.domain.entity.TodoShell
import java.time.Instant


fun ImportanceDto.toUi(): Importance = when (this) {
    ImportanceDto.LOW -> Importance.LOW
    ImportanceDto.REGULAR -> Importance.REGULAR
    ImportanceDto.URGENT -> Importance.URGENT
}

fun Importance.toDto(): ImportanceDto = when (this) {
    Importance.LOW -> ImportanceDto.LOW
    Importance.REGULAR -> ImportanceDto.REGULAR
    Importance.URGENT -> ImportanceDto.URGENT
}

fun Importance.toBody(): String = when (this) {
    Importance.LOW -> "low"
    Importance.REGULAR -> "basic"
    Importance.URGENT -> "important"
}


fun TodoItem.toBody(): TodoBody {
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

fun TodoItemApi.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance.toImportance().toDto(),
    deadline = deadline,
    flag = done,
    color = color,
    dateOfCreating = createdAt,
    dateOfEditing = changedAt
)

fun List<TodoItemApi>.toEntity() = this.map { it.toEntity() }

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance.toDto(),
    deadline = deadline,
    flag = isCompleted,
    dateOfCreating = dateOfCreating,
    dateOfEditing = dateOfEditing
)

@JvmName("toEntityTodoItemList")
fun List<TodoItem>.toEntity() = map { it.toEntity() }

fun TodoItemEntity.toUi() = TodoItem(
    id, text, importance.toUi(), deadline, flag, color, dateOfCreating
)

@JvmName("toUiTodoItemEntity")
fun List<TodoItemEntity>.toUi(): TodoShell = TodoShell(
    true,
    isError = false,
    todoItem = this.map { it.toUi() }
)

fun String.toImportance(): Importance = when (this) {
    "low" -> Importance.LOW
    "basic" -> Importance.REGULAR
    "important" -> Importance.URGENT
    else -> throw RuntimeException()
}

fun TodoItemApi.toUi(): TodoItem = TodoItem(
    id,
    text,
    importance.toImportance(),
    deadline,
    done,
    color,
    createdAt,
    changedAt
)

@JvmName("toUiTodoItemApi")
fun List<TodoItemApi>.toUi(): TodoShell = TodoShell(
    false,
    isError = false,
    todoItem = this.map { it.toUi() }
)