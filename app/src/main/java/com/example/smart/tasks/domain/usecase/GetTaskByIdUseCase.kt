package com.example.smart.tasks.domain.usecase

import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(taskId: String): Flow<Task?> {
        return repository.getTaskById(taskId)
    }
}