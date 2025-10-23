package com.example.smart.tasks.domain.repository

import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<Response<List<Task>>>

    fun getTaskById(taskId: String): Flow<Task?>

    suspend fun updateTaskStatusAndComment(taskId: String, status: TaskStatus, comment: String?)
}