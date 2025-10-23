package com.example.smart.tasks.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("id")
    val id: String,

    @SerialName("TargetDate")
    val targetDate: String,

    @SerialName("DueDate")
    val dueDate: String? = null,

    @SerialName("Title")
    val title: String,

    @SerialName("Description")
    val description: String? = null,

    @SerialName("Priority")
    val priority: Int = 0
)