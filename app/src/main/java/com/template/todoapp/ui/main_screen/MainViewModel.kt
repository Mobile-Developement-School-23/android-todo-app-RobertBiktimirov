package com.template.todoapp.ui.main_screen

import androidx.lifecycle.ViewModel
import com.template.todoapp.data.TodoItemRepository

class MainViewModel : ViewModel() {

    private val todoItemRepository = TodoItemRepository

    val todoList = todoItemRepository.todoItemsFlow

    fun addTodoItem() {

    }

    fun updateTodoItem(){

    }

    fun deleteTodoItem() {

    }
}