package com.template.todoapp.ui.task_screen

import androidx.lifecycle.ViewModel
import com.template.todoapp.data.TodoItemRepository
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {

    private val todoItemRepository = TodoItemRepository

    private val _todoItemState = MutableStateFlow<TodoItem?>(null)
    val todoItemState = _todoItemState.asStateFlow()

    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline get() = _deadline.asStateFlow()

    private val _nullErrorText = MutableStateFlow(false)
    val nullErrorText = _nullErrorText.asStateFlow()

    private val taskText = MutableStateFlow("")

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error = _error.asStateFlow()

    private val _closeScreen = MutableStateFlow(false)
    val closeScreen = _closeScreen.asStateFlow()

    fun setTaskText(text: String) {
        taskText.tryEmit(text)
        _nullErrorText.tryEmit(false)
    }

    fun setDeadline(deadline: Long?) {
        _deadline.tryEmit(deadline)
    }

    fun saveTask(importance: Importance, dateOfCreating: Long) {

        if (taskText.value.isNotEmpty()) {
            if (_todoItemState.value == null) {
                todoItemRepository.addTodoItem(
                    TodoItem(
                        generateTodoId(),
                        taskText.value,
                        importance,
                        deadline.value,
                        false,
                        dateOfCreating,
                        null
                    )
                )
                _closeScreen.tryEmit(true)
            }


        } else {
            _nullErrorText.tryEmit(true)
        }
    }

    fun deleteTodo() {
        todoItemRepository.deleteTodo(_todoItemState.value)
    }

    private fun generateTodoId() = "randomId ${taskText.value.hashCode()}"
    fun setTodo(todoItem: TodoItem?) {
        _todoItemState.tryEmit(todoItem)
    }
}