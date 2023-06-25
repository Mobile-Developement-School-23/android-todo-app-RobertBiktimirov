package com.template.api.services

import com.template.api.entity.TodoResponse
import retrofit2.http.GET

interface TodoService {


    @GET("list")
    suspend fun getTodoList(): TodoResponse

}