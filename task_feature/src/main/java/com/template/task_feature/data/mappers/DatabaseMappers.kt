package com.template.todoapp.data.mappers

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

fun List<TodoItemEntity>.toUi() = this.map { it.toUi() }