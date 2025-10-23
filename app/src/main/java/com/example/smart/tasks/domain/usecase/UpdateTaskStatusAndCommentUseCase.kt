package com.example.smart.tasks.domain.usecase

import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskStatusAndCommentUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, status: TaskStatus, comment: String?) {
        repository.updateTaskStatusAndComment(taskId, status, comment)
    }
}