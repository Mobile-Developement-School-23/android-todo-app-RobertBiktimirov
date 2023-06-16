package com.template.todoapp.data.database.dao

import androidx.room.*
import com.template.todoapp.data.database.entity.TodoItemEntity
import com.template.todoapp.domain.Importance
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


    /*
    * @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var internalId: Int = 0,
    @ColumnInfo("todo_id") val id: String,
    @ColumnInfo("text") val text: String,
    @TypeConverters(ImportanceConverter::class)
    @ColumnInfo("importance") val importance: Importance,
    @ColumnInfo("deadline") val deadline: Long?,
    @ColumnInfo("flag") var flag: Boolean,
    @ColumnInfo("dateOfCreating") val dateOfCreating: Long,
    @ColumnInfo("dateOfEditing") var dateOfEditing: Long?
    * */

    @Query(
        "update todoItem set text = :text, importance = :importance, " +
                "deadline = :deadline, flag = :flag, " +
                "dateOfEditing = :dateOfEditing where todo_id = :id"
    )
    suspend fun updateTodoItem(
        text: String,
        importance: Importance,
        deadline: Long?,
        flag: Int,
        dateOfEditing: Long?,
        id: String
    )

}