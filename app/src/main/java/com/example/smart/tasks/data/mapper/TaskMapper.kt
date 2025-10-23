package com.example.smart.tasks.data.mapper

import com.example.smart.tasks.data.local.model.TaskEntity
import com.example.smart.tasks.data.remote.dto.TaskDto
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.utils.calculateDaysLeft
import com.example.smart.tasks.utils.formatDate

fun TaskDto.toEntity(): TaskEntity = TaskEntity(
    id = id,
    targetDate = targetDate,
    dueDate = dueDate,
    title = title,
    description = description,
    priority = priority
)

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    targetDate = targetDate,
    dueDate = dueDate?.let { formatDate(it) },
    title = title,
    description = description,
    priority = priority,
    daysLeft = calculateDaysLeft(dueDate),
    status = TaskStatus.fromValue(status),
    comment = comment.orEmpty()
)