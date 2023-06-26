package com.template.task_feature.domain.entity


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
    var color: String? = null,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null
)
