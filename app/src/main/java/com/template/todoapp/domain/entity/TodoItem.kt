package com.template.todoapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date


enum class Importance {
    LOW,
    REGULAR,
    URGENT
}

@Parcelize
data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Long?,
    var isCompleted: Boolean = false,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null
): Parcelable
