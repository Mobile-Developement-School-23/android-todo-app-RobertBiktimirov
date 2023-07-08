package com.template.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.template.database.entity.YandexAccountDto

@Dao
interface YandexAccountDao {

    @Query("select * from yandex_account")
    suspend fun getDataYandexAccountCache(): YandexAccountDto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setYandexAccount(yandexAccountDto: YandexAccountDto)

    @Query("delete from yandex_account")
    suspend fun deleteAccount()
}