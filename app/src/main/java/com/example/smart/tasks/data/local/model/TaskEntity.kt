package com.example.smart.tasks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val targetDate: String,
    val dueDate: String? = null,
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    var status: Int = 0,
    var comment: String? = null
)