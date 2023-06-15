package com.template.todoapp.data.database.dao

import androidx.room.*
import com.template.todoapp.data.database.entity.TodoItemEntity
import com.template.todoapp.domain.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("select * from todoItem")
    fun getTodoItems(): Flow<List<TodoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItem(todoItemEntity: TodoItemEntity)

    @Query("delete from todoItem where todo_id = :id")
    suspend fun deleteTodoItem(id: String)

    @Update(entity = TodoItemEntity::class)
    suspend fun updateTodoItem(todoItemEntity: TodoItemEntity)

}