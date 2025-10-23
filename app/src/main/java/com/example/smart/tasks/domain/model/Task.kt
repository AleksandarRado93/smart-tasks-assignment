package com.example.smart.tasks.domain.model

data class Task(
    val id: String,
    val targetDate: String,
    val dueDate: String? = null,
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    val daysLeft: Int,
    var status: TaskStatus = TaskStatus.UNRESOLVED,
    var comment: String = "",
)

enum class TaskStatus(val value: Int) {
    UNRESOLVED(0),
    RESOLVED(1),
    CANT_RESOLVE(2);

    companion object {
        fun fromValue(value: Int): TaskStatus {
            return entries.firstOrNull { it.value == value } ?: UNRESOLVED
        }
    }
}
