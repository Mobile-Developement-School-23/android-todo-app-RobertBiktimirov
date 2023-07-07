package com.template.task_feature.ui.task_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.task_feature.domain.entity.*
import com.template.task_feature.domain.usecase.DeleteTodoUseCase
import com.template.task_feature.domain.usecase.GetTodoListUseCase
import com.template.task_feature.domain.usecase.LoadTodoListInDbUseCase
import com.template.task_feature.domain.usecase.UpdateTodoListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class TaskListViewModel @Inject constructor(
    getTodoListUseCase: GetTodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val firstLoadTodoListUseCase: LoadTodoListInDbUseCase
) : ViewModel() {

    private val _noInternet = MutableStateFlow(false)
    val noInternet get() = _noInternet.asStateFlow()

    private val _startAddButtonAnim = MutableStateFlow(true)
    val startAddButtonAnim = _startAddButtonAnim.asStateFlow()

    var isVisibleDone: Boolean = false
        set(value) {
            field = value
            _isVisibleDoneTask.tryEmit(value)
        }

    val todoList: StateFlow<TodoShell> = getTodoListUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, TodoShell.toEmpty())

    private val _isVisibleDoneTask: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVisibleDoneTask get() = _isVisibleDoneTask.asStateFlow()

    private val _emptinessTodoList = MutableStateFlow(false)
    val emptinessTodoList = _emptinessTodoList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            firstLoadTodoListUseCase()
        }
    }


    fun setStateAnim(flag: Boolean){
        _startAddButtonAnim.tryEmit(flag)
    }

    fun setIsEmptyList(flag: Boolean) {
        _emptinessTodoList.tryEmit(flag)
    }

    fun updateTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            firstLoadTodoListUseCase()
        }
    }

    fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            handleUpdateOrSaveResult(updateTodoListUseCase(todoItem))
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
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