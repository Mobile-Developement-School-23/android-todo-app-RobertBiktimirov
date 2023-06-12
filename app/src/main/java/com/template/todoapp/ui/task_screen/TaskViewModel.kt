package com.template.todoapp.ui.task_screen

import androidx.lifecycle.ViewModel
import com.template.todoapp.data.TodoItemRepository
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {

    private val todoItemRepository = TodoItemRepository

    val todoItem = MutableStateFlow<TodoItem?>(null)
    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    private val taskText = MutableStateFlow("")

    fun setTaskText(text: String) {
        taskText.tryEmit(text)
    }

    fun setDeadline(deadline: Long) {
        _deadline.tryEmit(deadline)
    }

    fun saveTask(importance: String) {
        if (taskText.value.isEmpty()) {

        }
    }
}