package com.example.smart.tasks.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateString: String?): String {
    return try {
        if (dateString.isNullOrBlank()) return "—"

        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.US)
        val date = LocalDate.parse(dateString, inputFormatter)
        date.format(outputFormatter)
    } catch (_: Exception) {
        "—"
    }
}

fun calculateDaysLeft(dateString: String?): Int {
    return try {
        if (dateString.isNullOrBlank()) return 0

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
        val dueDate = LocalDate.parse(dateString, formatter)
        val today = LocalDate.now()

        Period.between(today, dueDate).days
    } catch (_: Exception) {
        0
    }
}