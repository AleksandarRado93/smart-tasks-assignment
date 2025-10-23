package com.example.smart.tasks.ui.navigation

object Graph {
    const val ROOT = "root_graph"
}

object Routes {
    const val TASK_LIST = "task_list"
    const val TASK_DETAILS = "task_details/{${Args.TASK_ID}}"

    fun taskDetails(taskId: String) = "task_details/$taskId"
}

object Args {
    const val TASK_ID = "taskId"
}