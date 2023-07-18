package com.template.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.template.database.dao.RequestDao
import com.template.database.dao.TodoDao
import com.template.database.dao.YandexAccountDao
import com.template.database.entity.RequestDto
import com.template.database.entity.TodoItemEntity
import com.template.database.entity.YandexAccountDto

@Database(
    entities = [TodoItemEntity::class, RequestDto::class, YandexAccountDto::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun requestDao(): RequestDao
    abstract fun yandexAccountDao(): YandexAccountDao

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