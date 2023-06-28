package com.template.task_feature.ui.task_list_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.task_feature.domain.entity.TodoItem
import com.template.todoapp.domain.usecase.DeleteTodoUseCase
import com.template.task_feature.domain.usecase.GetTodoListUseCase
import com.template.todoapp.domain.usecase.UpdateTodoListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.template.common.utli.runCatchingNonCancellation
import com.template.task_feature.domain.entity.TodoShell
import com.template.task_feature.domain.usecase.LoadTodoListInDbUseCase
import kotlinx.coroutines.flow.*

class TaskListViewModel @Inject constructor(
    getTodoListUseCase: GetTodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val firstLoadTodoListUseCase: LoadTodoListInDbUseCase
) : ViewModel() {

    private val _noInternet = MutableStateFlow(false)
    val noInternet get() = _noInternet.asStateFlow()

    init {
        viewModelScope.launch {
            runCatchingNonCancellation {
                firstLoadTodoListUseCase()
            }.getOrElse {
                Log.d("no network and error", "not network :)")
            }
        }
    }

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

    fun updateTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                updateTodoListUseCase(todoItem)
            }
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            runCatchingNonCancellation {
                deleteTodoUseCase(todoItem.id)
            }
        }
    }
}