package com.template.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.template.database.converter.ImportanceConverter
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.database.entity.RequestDto
import com.template.database.entity.TodoItemEntity

@Database(entities = [TodoItemEntity::class, RequestDto::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun requestDao(): RequestDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "todoApp.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}