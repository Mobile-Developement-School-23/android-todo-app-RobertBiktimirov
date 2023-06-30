package com.template.task_feature.ui.task_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.task_feature.domain.entity.*
import com.template.task_feature.domain.usecase.DeleteTodoUseCase
import com.template.task_feature.domain.usecase.GetTodoListUseCase
import com.template.task_feature.domain.usecase.LoadTodoListInDbUseCase
import com.template.task_feature.domain.usecase.UpdateTodoListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    getTodoListUseCase: GetTodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val firstLoadTodoListUseCase: LoadTodoListInDbUseCase
) : ViewModel() {

    private val _noInternet = MutableStateFlow(false)
    val noInternet get() = _noInternet.asStateFlow()

    var isVisibleDone: Boolean = false
        set(value) {
            field = value
            _isVisibleDoneTask.tryEmit(value)
        }

    val todoList = getTodoListUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, TodoShell.toEmpty())

    private val _isVisibleDoneTask: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVisibleDoneTask get() = _isVisibleDoneTask.asStateFlow()

    private val _emptinessTodoList = MutableStateFlow(false)
    val emptinessTodoList = _emptinessTodoList.asStateFlow()

    fun setIsEmptyList(flag: Boolean) {
        _emptinessTodoList.tryEmit(flag)
    }

    fun setAnswerByInternet(flag: Boolean){
        _noInternet.tryEmit(flag)
    }

    fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            handleUpdateOrSaveResult(updateTodoListUseCase(todoItem))
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            handleUpdateOrSaveResult(deleteTodoUseCase(todoItem))
        }
    }


    private fun handleUpdateOrSaveResult(result: RepositoryResult<TodoItem>) {

        when (result) {
            is RepositoryError -> {
                Log.d("connection test", "${result.code} ${result.message}")
            }
            is RepositoryException -> {
                Log.d("connection test", result.e.message.toString())
                _noInternet.tryEmit(true)
            }
            is RepositorySuccess -> {
//                Log.d("connection test", "success delete")
            }
        }
    }
}