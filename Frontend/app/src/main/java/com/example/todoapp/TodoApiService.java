package com.example.todoapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TodoApiService {
    @GET("/api/todo")
    @Headers("Accept: application/json")
    Call<List<TodoItem>> getTodos(@Query("offset") int offset, @Query("limit") int limit);

    @POST("/api/todo")
    Call<Void> addTodo(@Body TodoItem todo);

    @PUT("/api/todo/{id}")
    Call<Void> updateTodo(@Path("id") int id, @Body TodoItem todo);

    @DELETE("/api/todo/{id}")
    Call<Void> deleteTodo(@Path("id") int id);
}
