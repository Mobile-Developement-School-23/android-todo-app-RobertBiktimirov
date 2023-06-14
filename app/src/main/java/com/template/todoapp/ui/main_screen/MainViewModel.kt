package com.template.todoapp.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.todoapp.data.TodoItemRepository
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val todoItemRepository = TodoItemRepository

    val todoList = todoItemRepository.todoItemsFlow

    fun deleteTodoItem() {

    }

    fun editTodo(todoItem: TodoItem){
        viewModelScope.launch {
            todoItemRepository.updateTodo(todoItem)
        }
    }
}