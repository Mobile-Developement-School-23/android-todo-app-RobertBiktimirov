package com.template.database.converter

import androidx.room.TypeConverter
import com.template.database.entity.ImportanceDto

class ImportanceConverter {

    @TypeConverter
    fun fromImportance(importance: ImportanceDto) = importance.name

    @TypeConverter
    fun toImportance(value: String) = enumValueOf<ImportanceDto>(value)
}