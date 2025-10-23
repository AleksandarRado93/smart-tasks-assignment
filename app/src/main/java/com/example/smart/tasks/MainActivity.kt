package com.example.smart.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smart.tasks.ui.navigation.Args
import com.example.smart.tasks.ui.navigation.Graph
import com.example.smart.tasks.ui.navigation.Routes
import com.example.smart.tasks.ui.screens.task_details.TaskDetailsScreen
import com.example.smart.tasks.ui.screens.task_details.TaskDetailsViewModel
import com.example.smart.tasks.ui.screens.task_list.TaskListScreen
import com.example.smart.tasks.ui.screens.task_list.TaskListViewModel
import com.example.smart.tasks.ui.theme.SmartTasksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartTasksTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    route = Graph.ROOT,
                    startDestination = Routes.TASK_LIST
                ) {
                    composable(route = Routes.TASK_LIST) {
                        val viewModel: TaskListViewModel = hiltViewModel()
                        val state = viewModel.uiState.collectAsStateWithLifecycle()

                        TaskListScreen(
                            state = state.value,
                            loadTaskForDate = viewModel::changeSelectedDate,
                            onTaskClick = { taskId ->
                                navController.navigate(Routes.taskDetails(taskId))
                            }
                        )
                    }

                    composable(
                        route = Routes.TASK_DETAILS,
                        arguments = listOf(navArgument(Args.TASK_ID) {
                            type = NavType.StringType
                        })
                    ) {
                        val viewModel: TaskDetailsViewModel = hiltViewModel()
                        val state = viewModel.uiState.collectAsStateWithLifecycle()

                        TaskDetailsScreen(
                            state = state.value,
                            onUpdateTaskStatusAndCommentClick = viewModel::updateTaskStatusAndComment
                        )
                    }
                }
            }
        }
    }
}