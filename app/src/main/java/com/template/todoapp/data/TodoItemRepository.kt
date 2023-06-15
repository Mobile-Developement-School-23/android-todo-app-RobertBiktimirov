package com.template.todoapp.data

import android.util.Log
import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

object TodoItemRepository {

    private val todoItems = mutableListOf<TodoItem>()
    val todoItemsFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(mutableListOf())

    init {
        for (i in 0..20) {
            addTodoItem(
                TodoItem(
                    "toDoItemId $i",
                    "$i купить сыр и сварить суп efpjp[jr[piij[pgj[pjpe[jpgrj[pgjpgerj[pregj[pj[prgj",
                    randomImportance(),
                    randomDeadline(),
                    Random.nextBoolean(),
                    3424368740 - 10000,
                    null
                )
            )
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        todoItems.add(todoItem)
        todoItemsFlow.tryEmit(todoItems)
    }

    fun updateTodo(todoItem: TodoItem) {
        val positionTodo = todoItems.indexOfFirst { it.id == todoItem.id }
        if (positionTodo != -1) {
            todoItems[positionTodo] = todoItem
            todoItemsFlow.tryEmit(todoItems)
        }
    }

    fun deleteTodo(todoItem: TodoItem?) {
        if (todoItem != null) {
            val newTodoItems = todoItems.filter { it != todoItem }
            todoItemsFlow.tryEmit(newTodoItems)
            todoItems.remove(todoItem)
        }
    }

    private fun randomImportance(): Importance {
        return when (Random.nextInt(0, 3)) {
            0 -> Importance.LOW
            1 -> Importance.REGULAR
            2 -> Importance.URGENT
            else -> throw Exception()
        }
    }


    private fun randomDeadline(): Long? {
        return when (Random.nextInt(0, 2)) {
            0 -> null
            1 -> 9424368740
            else -> null
        }
    }
}