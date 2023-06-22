package com.template.todoapp.data.mappers

import com.template.todoapp.data.database.entity.TodoItemEntity
import com.template.todoapp.domain.entity.TodoItem

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    flag = isCompleted,
    dateOfCreating = dateOfCreating,
    dateOfEditing = dateOfEditing
)

fun TodoItemEntity.toUi() = TodoItem(
    id, text, importance, deadline, flag, dateOfCreating
)

fun List<TodoItemEntity>.toUi() = this.map { it.toUi() }