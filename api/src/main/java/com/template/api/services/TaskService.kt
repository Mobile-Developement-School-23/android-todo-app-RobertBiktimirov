package com.template.api.services

import com.template.api.entity.TodoBody
import com.template.api.entity.TodoListBody
import com.template.api.entity.TodoListResponse
import com.template.api.entity.TodoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("list")
    suspend fun getTodoList(): Response<TodoListResponse>

    @GET("list/{id}")
    suspend fun getTodoItem(
        @Path("id") todoId: String
    ): Response<TodoResponse>


    @POST("list")
    suspend fun saveTodoItem(
        @Body todoBody: TodoBody,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoResponse>


    @PUT("list/{id}")
    suspend fun editTodoItem(
        @Path("id") todoId: String,
        @Body todoBody: TodoBody,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoResponse>

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") todoId: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoResponse>

    @PATCH("list")
    suspend fun uniteListInApi(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoList: TodoListBody
    ): Response<TodoListResponse>

}