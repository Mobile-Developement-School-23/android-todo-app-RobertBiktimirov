package com.template.todoapp.domain


enum class Importance {
    LOW,
    REGULAR,
    URGENT
}

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long?,
    var flag: Boolean = false,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null
)
