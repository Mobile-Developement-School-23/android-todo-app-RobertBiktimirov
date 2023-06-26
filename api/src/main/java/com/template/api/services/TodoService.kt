package com.template.api.services

import com.template.api.entity.TodoBody
import com.template.api.entity.TodoListResponse
import com.template.api.entity.TodoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoService {

    @GET("list")
    suspend fun getTodoList(): TodoListResponse

    @GET("list/{id}")
    suspend fun getTodoItem(
        @Path("id") todoId: String
    ): TodoResponse


    @POST("list")
    suspend fun saveTodoItem(
        @Body todoBody: TodoBody,
        @Header("X-Last-Known-Revision") revision: Int
    ): TodoResponse


    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Path("id") todoId: String
    )


}