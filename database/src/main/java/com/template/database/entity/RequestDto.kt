package com.template.database.entity

import androidx.room.*
import com.template.database.converter.TodoItemConverter
import com.template.database.converter.ViewRequestsConverter


enum class ViewRequest {
    UPDATE,
    DELETE,
    SAVE
}

@Entity(tableName = "requests")
data class RequestDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("random_id") var id: Int = 0,
    @TypeConverters(ViewRequestsConverter::class)
    @ColumnInfo("view") val view: ViewRequest,
    @Embedded var todoItemEntity: TodoItemEntity,
    @ColumnInfo("key_id") val keyId: String
)