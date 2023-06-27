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

data class TodoShell(
    val isCache: Boolean,
    val isError: Boolean,
    val todoItem: List<TodoItem>
) {
    companion object {
        fun toEmpty() = TodoShell(isCache = true, isError = false, todoItem = emptyList())

        fun toError() = TodoShell(isCache = false, isError = true, todoItem = emptyList())
    }

}
