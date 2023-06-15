package com.template.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.template.todoapp.data.database.dao.TodoDao
import com.template.todoapp.data.database.entity.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

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
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}