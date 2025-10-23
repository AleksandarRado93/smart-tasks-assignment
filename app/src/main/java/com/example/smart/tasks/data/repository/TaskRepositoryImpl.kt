package com.example.smart.tasks.data.repository

import com.example.smart.tasks.data.local.dao.TaskDao
import com.example.smart.tasks.data.local.model.TaskEntity
import com.example.smart.tasks.data.mapper.toDomain
import com.example.smart.tasks.data.mapper.toEntity
import com.example.smart.tasks.data.remote.api.ApiService
import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val api: ApiService,
    private val dao: TaskDao
) : TaskRepository {
    override fun getTasks(): Flow<Response<List<Task>>> = flow {
        val cached = dao.getAllTasks().first()

        val result = runCatching {
            val remote = api.getTasks()
            val remoteEntities = remote.tasks.map { it.toEntity() }
            mergeRemoteStatusAndCommentWithLocal(remoteEntities, cached)
        }

        result.fold(
            onSuccess = { merged ->
                dao.insertTasks(merged)
            },
            onFailure = { throwable ->
                emit(
                    Response.Error(
                        throwable.message ?: "Unknown error",
                        cached.map { it.toDomain() })
                )
            }
        )

        dao.getAllTasks().collect { entities ->
            if (entities.isNotEmpty()) {
                emit(Response.Success(entities.map { it.toDomain() }))
            }
        }
    }

    private fun mergeRemoteStatusAndCommentWithLocal(remoteList: List<TaskEntity>, local: List<TaskEntity>) =
        remoteList.map { remote ->
            local.find { it.id == remote.id }?.let {
                remote.copy(
                    status = it.status,
                    comment = it.comment
                )
            } ?: remote
        }


    override fun getTaskById(taskId: String): Flow<Task?> =
        dao.getTaskById(taskId).map { it?.toDomain() }

    override suspend fun updateTaskStatusAndComment(
        taskId: String,
        status: TaskStatus,
        comment: String?
    ) {
        dao.updateTaskStatusAndComment(taskId, status.value, comment)
    }
}