package com.template.task_feature.ui.task_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.task_feature.domain.entity.*
import com.template.task_feature.domain.usecase.DeleteTodoUseCase
import com.template.task_feature.domain.usecase.GetTodoItemUseCase
import com.template.task_feature.domain.usecase.SaveTodoItemUseCase
import com.template.task_feature.domain.usecase.UpdateTodoListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    private val _noInternet = MutableStateFlow(false)
    val noInternet = _noInternet.asStateFlow()

    fun setNoInternet(flag: Boolean){
        _noInternet.tryEmit(flag)
        _closeScreen.tryEmit(!flag)
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
            _loadingStatus.tryEmit(true)

            when (val todoItem = getTodoItemUseCase.invoke(id)) {
                is RepositoryError -> {
                    _error.tryEmit(true)
                    _loadingStatus.tryEmit(false)
                }
                is RepositoryException -> {
                    _error.tryEmit(true)
                    _loadingStatus.tryEmit(false)
                }
                is RepositorySuccess -> {
                    _todoItemState.tryEmit(todoItem.data)
                    _loadingStatus.tryEmit(false)
                }
            }

        }
    }

    fun saveTask(importance: Importance, dateOfCreating: Long) {
        viewModelScope.launch {
            saveOrUpdateTask(importance, dateOfCreating)
        }
    }

    fun deleteTodo() {
        viewModelScope.launch {
            _todoItemState.value?.let {
                handleUpdateOrSaveResult(deleteTodoUseCase(it))
            }
        }
    }

    fun closeTheScreen() {
        _closeScreen.tryEmit(true)
    }

    private suspend fun saveOrUpdateTask(importance: Importance, dateOfCreating: Long) {

        if (taskText.value.isNotEmpty()) {
            if (_todoItemState.value == null) {
                handleUpdateOrSaveResult(
                    saveTodoItemUseCase(
                        TodoItem(
                            generateTodoId(),
                            taskText.value,
                            importance,
                            deadline.value,
                            false,
                            "#000000",
                            dateOfCreating,
                            null
                        )
                    )
                )
            } else {
                todoItemState.value?.let {
                    handleUpdateOrSaveResult(
                        updateTodoListUseCase(
                            TodoItem(
                                it.id,
                                taskText.value,
                                importance,
                                deadline.value,
                                it.isCompleted,
                                "#000000",
                                it.dateOfCreating,
                                Calendar.getInstance().timeInMillis
                            )
                        )
                    )
                }
            }
        } else {
            _nullErrorText.emit(true)
        }
    }


    private fun handleUpdateOrSaveResult(result: RepositoryResult<TodoItem>) {

        when (result) {
            is RepositoryError -> {
                Log.d("connection test", "${result.code} ${result.message}")
            }
            is RepositoryException -> {
                _noInternet.tryEmit(true)
            }
            is RepositorySuccess -> {
                _closeScreen.tryEmit(true)
            }
        }
    }


    private fun generateTodoId() =
        "${taskText.value.hashCode()} - ${Calendar.getInstance().timeInMillis}"
}