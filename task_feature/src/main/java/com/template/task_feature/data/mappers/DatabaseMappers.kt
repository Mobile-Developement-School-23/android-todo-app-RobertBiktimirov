package com.template.todoapp.data.mappers

import com.template.api.entity.TodoItemApi
import com.template.database.entity.ImportanceDto
import com.template.database.entity.TodoItemEntity
import com.template.task_feature.domain.entity.Importance
import com.template.task_feature.domain.entity.TodoItem


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

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance.toDto(),
    deadline = deadline,
    flag = isCompleted,
    dateOfCreating = dateOfCreating,
    dateOfEditing = dateOfEditing
)

fun TodoItemEntity.toUi() = TodoItem(
    id, text, importance.toUi(), deadline, flag, dateOfCreating
)

@JvmName("toUiTodoItemEntity")
fun List<TodoItemEntity>.toUi() = this.map { it.toUi() }

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
    createdAt,
    changedAt
)

@JvmName("toUiTodoItemApi")
fun List<TodoItemApi>.toUi() = this.map { it.toUi() }