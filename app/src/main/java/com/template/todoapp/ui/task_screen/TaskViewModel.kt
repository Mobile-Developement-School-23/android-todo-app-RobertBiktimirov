package com.template.todoapp.ui.task_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.todoapp.domain.entity.Importance
import com.template.todoapp.domain.entity.TodoItem
import com.template.todoapp.domain.usecase.DeleteTodoUseCase
import com.template.todoapp.domain.usecase.GetTodoItemUseCase
import com.template.todoapp.domain.usecase.SaveTodoItemUseCase
import com.template.todoapp.domain.usecase.UpdateTodoListUseCase
import com.template.todoapp.ui.utli.runCatchingNonCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val saveTodoItemUseCase: SaveTodoItemUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val getTodoItemUseCase: GetTodoItemUseCase
) : ViewModel() {

    private val _loadingStatus = MutableStateFlow(false)
    val loadingStatus get() = _loadingStatus.asStateFlow()

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

    private fun setTodo(todoItem: TodoItem) {
        _todoItemState.tryEmit(todoItem)
        setLoadingStatus(false)
    }

    fun setTaskText(text: String) {
        taskText.tryEmit(text)
        _nullErrorText.tryEmit(false)
    }


    fun setDeadline(deadline: Long?) {
        _deadline.tryEmit(deadline)
    }

    fun getTodoItem(id: String) {
        viewModelScope.launch {
            setLoadingStatus(true)
            val todo = runCatchingNonCancellation { getTodoItemUseCase(id) }.getOrNull()
            if (todo != null) {
                setTodo(todo)
            } else {
                _error.tryEmit(true)
            }
        }
    }

    fun saveTask(importance: Importance, dateOfCreating: Long) {
        viewModelScope.launch {
            saveOrUpdateTask(importance, dateOfCreating)
        }
    }

    private fun setLoadingStatus(flag: Boolean) {
        _loadingStatus.tryEmit(flag)
    }

    fun deleteTodo() {
        viewModelScope.launch {
            _todoItemState.value?.let {
                deleteTodoUseCase(it.id)
            }
        }
    }

    private suspend fun saveOrUpdateTask(importance: Importance, dateOfCreating: Long) {

        if (taskText.value.isNotEmpty()) {
            if (_todoItemState.value == null) {
                saveTodoItemUseCase(
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
            } else {
                todoItemState.value?.let {
                    updateTodoListUseCase(
                        TodoItem(
                            it.id,
                            taskText.value,
                            importance,
                            deadline.value,
                            it.isCompleted,
                            it.dateOfCreating,
                            Calendar.getInstance().timeInMillis
                        )
                    )
                }

                _closeScreen.tryEmit(true)
            }
        } else {
            _nullErrorText.tryEmit(true)
        }
    }

    private fun generateTodoId() =
        "${taskText.value.hashCode()} - ${Calendar.getInstance().timeInMillis}"
}