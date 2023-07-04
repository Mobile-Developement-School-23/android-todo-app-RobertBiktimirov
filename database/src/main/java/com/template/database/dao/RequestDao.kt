package com.template.database.dao

import androidx.room.*
import com.template.database.entity.RequestDto

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRequest(requestDto: RequestDto)

    @Query("select * from requests")
    suspend fun getRequest(): List<RequestDto>

    @Query("delete from requests")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteRequest(requestDto: RequestDto)

}