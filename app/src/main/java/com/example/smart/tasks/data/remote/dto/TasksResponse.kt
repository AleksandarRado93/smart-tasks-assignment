package com.example.smart.tasks.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TasksResponse(
    @SerialName("tasks")
    val tasks: List<TaskDto>
)
