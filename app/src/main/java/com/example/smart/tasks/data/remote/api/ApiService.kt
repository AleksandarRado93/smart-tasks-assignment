package com.example.smart.tasks.data.remote.api

import com.example.smart.tasks.data.remote.dto.TasksResponse
import retrofit2.http.GET

interface ApiService {
    @GET(".")
    suspend fun getTasks(): TasksResponse
}