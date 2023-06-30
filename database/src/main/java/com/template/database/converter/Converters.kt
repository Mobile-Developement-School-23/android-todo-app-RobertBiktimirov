package com.template.database.converter

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.template.database.entity.ImportanceDto
import com.template.database.entity.TodoItemEntity
import com.template.database.entity.ViewRequest

class ImportanceConverter {

    @TypeConverter
    fun fromImportance(importance: ImportanceDto) = importance.name

    @TypeConverter
    fun toImportance(value: String) = enumValueOf<ImportanceDto>(value)
}


class ViewRequestsConverter {
    @TypeConverter
    fun fromImportance(view: ViewRequest) = view.name

    @TypeConverter
    fun toImportance(value: String) = enumValueOf<ViewRequest>(value)
}

class TodoItemConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromTodoItem(todo: TodoItemEntity) = gson.toJson(todo)

    @TypeConverter
    fun toTodoItem(json: String): TodoItemEntity {
        val todoItem = object : TypeToken<TodoItemEntity>() {}
        return gson.fromJson(json, todoItem)
    }


}