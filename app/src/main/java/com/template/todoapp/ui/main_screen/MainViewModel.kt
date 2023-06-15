package com.template.todoapp.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.todoapp.domain.TodoItem
import com.template.todoapp.domain.usecase.DeleteTodoUseCase
import com.template.todoapp.domain.usecase.GetTodoListUseCase
import com.template.todoapp.domain.usecase.UpdateTodoListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getTodoListUseCase: GetTodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase
) : ViewModel() {

    val todoList = getTodoListUseCase()

    private val _emptinessTodoList = MutableStateFlow(false)
    val emptinessTodoList = _emptinessTodoList.asStateFlow()

    fun setIsEmptyList(flag: Boolean){
        _emptinessTodoList.tryEmit(flag)
    }

    fun updateTodo(todoItem: TodoItem){
        viewModelScope.launch {
            updateTodoListUseCase(todoItem)
        }
    }

    fun deleteTodo(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteTodoUseCase(todoItem)
        }
    }
}