package com.template.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TodoItemApi(
    @SerialName("id") var id: String,
    @SerialName("text") var text: String,
    @SerialName("importance") var importance: String,
    @SerialName("deadline") var deadline: Long? = null,
    @SerialName("done") var done: Boolean,
    @SerialName("color") var color: String? = null,
    @SerialName("created_at") var createdAt: Long,
    @SerialName("changed_at") var changedAt: Long? = null,
    @SerialName("last_updated_by") var lastUpdateBy: String
)


@Serializable
data class TodoListResponse(
    @SerialName("status") val status: String,
    @SerialName("list") val list: List<TodoItemApi>,
    @SerialName("revision") val revision: Int
)

@Serializable
data class TodoResponse(
    @SerialName("status") val status: String,
    @SerialName("element") val todoItem: TodoItemApi,
    @SerialName("revision") val revision: Int
)


@Serializable
data class TodoBody(
    @SerialName("element") val todoItem: TodoItemApi,
)
