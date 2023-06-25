package com.template.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TodoItemApi(
    @SerialName("id") var id: String = "",
    @SerialName("text") var text: String = "",
    @SerialName("importance") var importance: String = "",
    @SerialName("deadline") var deadline: Long = 0L,
    @SerialName("done") var done: Boolean = false,
    @SerialName("color") var color: String = "",
    @SerialName("created_at") var createdAt: Long = 0L,
    @SerialName("changed_at") var changedAt: Long = 0L,
    @SerialName("last_update_by") var lastUpdateBy: String = ""
)


@Serializable
data class TodoResponse(
    @SerialName("status") val status: String,
    @SerialName("list") val list: List<TodoItemApi>,
    @SerialName("revision") val revision: Int
)
