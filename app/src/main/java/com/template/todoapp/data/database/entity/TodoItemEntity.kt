package com.template.todoapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.template.todoapp.data.database.converter.ImportanceConverter
import com.template.todoapp.domain.entity.Importance

@Entity(tableName = "todoItem")
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var internalId: Int = 0,
    @ColumnInfo("todo_id") val id: String,
    @ColumnInfo("text") val text: String,
    @TypeConverters(ImportanceConverter::class)
    @ColumnInfo("importance") val importance: Importance,
    @ColumnInfo("deadline") val deadline: Long?,
    @ColumnInfo("flag") var flag: Boolean,
    @ColumnInfo("dateOfCreating") val dateOfCreating: Long,
    @ColumnInfo("dateOfEditing") var dateOfEditing: Long?
)
