package com.example.smart.tasks.domain.usecase

import com.example.smart.tasks.domain.model.Task
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateUseCase @Inject constructor() {
    operator fun invoke(date: LocalDate, tasks: List<Task>): List<Task> {
        return tasks
            .filter { it.targetDate == date.toString() }
            .sortedWith(
                compareByDescending<Task> { it.priority }
                    .thenBy { it.dueDate ?: it.targetDate }
            )
    }
}