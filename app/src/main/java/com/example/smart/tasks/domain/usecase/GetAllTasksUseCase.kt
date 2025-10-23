package com.example.smart.tasks.domain.usecase

import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<Response<List<Task>>> {
        return repository.getTasks()
    }
}