package com.template.todoapp.domain.entity


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
    var isCompleted: Boolean = false,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null
)
