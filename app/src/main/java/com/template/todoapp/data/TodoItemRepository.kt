package com.template.todoapp.data

import com.template.todoapp.domain.Importance
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class TodoItemRepository {

    private val todoItems = mutableListOf<TodoItem>()
    val todoItemsFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())

    init {
        for (i in 0..20) {
            addToDoItem(
                TodoItem(
                    "toDoItemId $i",
                    "купить сыр и сварить суп",
                    randomImportance(),
                    randomDeadline(),
                    Random.nextBoolean(),
                    3424368740 - 10000,
                    null
                )
            )
        }
    }

    fun addToDoItem(todo: TodoItem) {
        todoItems.add(todo)
        todoItemsFlow.tryEmit(todoItems)
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
        return when(Random.nextInt(0, 2)) {
            0 -> null
            1 -> 3424368740
            else -> null
        }
    }

}