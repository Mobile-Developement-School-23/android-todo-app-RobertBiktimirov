package com.template.todoapp.data.mappers

import com.template.todoapp.data.database.entity.TodoItemEntity
import com.template.todoapp.domain.TodoItem

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    text = text,
    importance = importance,
    deadline = deadline,
    flag = flag,
    dateOfCreating = dateOfCreating,
    dateOfEditing = dateOfEditing
)

fun TodoItemEntity.toUi() = TodoItem(
    id, text, importance, deadline, flag, dateOfCreating
)

fun List<TodoItemEntity>.toUi() = this.map { it.toUi() }