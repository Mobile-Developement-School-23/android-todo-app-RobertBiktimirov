package com.template.todoapp.data.database.converter

import androidx.room.TypeConverter
import com.template.todoapp.domain.Importance

class ImportanceConverter {

    @TypeConverter
    fun fromImportance(importance: Importance) = importance.name

    @TypeConverter
    fun toImportance(value: String) = enumValueOf<Importance>(value)
}