package com.template.database.entity

import androidx.room.*
import com.template.database.converter.ImportanceConverter


enum class ImportanceDto {
    LOW,
    REGULAR,
    URGENT
}

@Entity(tableName = "todoItem")
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") var internalId: Int = 0,
    @ColumnInfo("todo_id") val id: String,
    @ColumnInfo("text") val text: String,
    @TypeConverters(ImportanceConverter::class)
    @ColumnInfo("importance") val importance: ImportanceDto,
    @ColumnInfo("deadline") val deadline: Long?,
    @ColumnInfo("flag") var flag: Boolean,
    @ColumnInfo("color") var color: String? = null,
    @ColumnInfo("dateOfCreating") val dateOfCreating: Long,
    @ColumnInfo("dateOfEditing") var dateOfEditing: Long?
)
