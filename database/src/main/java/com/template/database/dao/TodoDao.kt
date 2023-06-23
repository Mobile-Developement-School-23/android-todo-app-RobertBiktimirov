package com.template.database.dao

import androidx.room.*
import com.template.database.entity.ImportanceDto
import com.template.database.entity.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("select * from todoItem")
    fun getTodoItems(): Flow<List<TodoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTodoItem(todoItemEntity: TodoItemEntity)

    @Query("delete from todoItem where todo_id = :id")
    suspend fun deleteTodoItem(id: String)

    @Query("select * from todoItem where todo_id = :id limit 1")
    suspend fun getTodoItem(id: String): TodoItemEntity

    @Query(
        "update todoItem set text = :text, importance = :importance, " +
                "deadline = :deadline, flag = :flag, " +
                "dateOfEditing = :dateOfEditing where todo_id = :id"
    )
    suspend fun updateTodoItem(
        text: String,
        importance: ImportanceDto,
        deadline: Long?,
        flag: Int,
        dateOfEditing: Long?,
        id: String
    )

}