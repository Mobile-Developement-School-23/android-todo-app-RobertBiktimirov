package com.template.database.dao

import androidx.room.*
import com.template.database.entity.ImportanceDto
import com.template.database.entity.RequestDto
import com.template.database.entity.ViewRequest

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

    @Query(
        "update requests set view = :view, text = :text, importance = :importance, " +
            "deadline = :deadline, flag = :flag, " +
            "dateOfEditing = :dateOfEditing where todo_id = :id"
    )
    suspend fun update(
        view: ViewRequest,
        text: String,
        importance: ImportanceDto,
        deadline: Long?,
        flag: Boolean,
        dateOfEditing: Long?,
        id: String,
    )

}