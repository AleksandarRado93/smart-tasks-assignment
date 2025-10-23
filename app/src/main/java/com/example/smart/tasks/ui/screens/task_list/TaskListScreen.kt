package com.example.smart.tasks.ui.screens.task_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.smart.tasks.R
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.ui.components.EmptyState
import com.example.smart.tasks.ui.components.ErrorScreen
import com.example.smart.tasks.ui.components.LoadingScreen
import com.example.smart.tasks.ui.components.TaskItem
import com.example.smart.tasks.ui.theme.WhiteCard
import com.example.smart.tasks.ui.theme.YellowBackground
import com.example.smart.tasks.utils.formatDate

private const val PREVIOUS_DAY = -1L
private const val NEXT_DAY = 1L

@Composable
fun TaskListScreen(
    state: TaskListViewModel.TaskListUiState,
    loadTaskForDate: (Long) -> Unit,
    onTaskClick: (String) -> Unit
) {
    when (state) {
        is TaskListViewModel.TaskListUiState.Loading -> LoadingScreen()

        is TaskListViewModel.TaskListUiState.Error -> ErrorScreen(state.message)

        is TaskListViewModel.TaskListUiState.Success -> TaskList(
            selectedDate = state.selectedDate.toString(),
            tasks = state.tasks,
            loadTaskForDate = loadTaskForDate,
            onTaskClick = onTaskClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList(
    selectedDate: String,
    tasks: List<Task>,
    loadTaskForDate: (Long) -> Unit,
    onTaskClick: (String) -> Unit
) {
    Scaffold(
        containerColor = YellowBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = YellowBackground,
                    titleContentColor = WhiteCard,
                    navigationIconContentColor = WhiteCard,
                    actionIconContentColor = WhiteCard
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { loadTaskForDate(PREVIOUS_DAY) }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.previous_day)
                            )
                        }

                        Text(
                            text = formatDate(selectedDate),
                            color = WhiteCard
                        )

                        IconButton(onClick = { loadTaskForDate(NEXT_DAY) }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = stringResource(R.string.next_day)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (tasks.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = onTaskClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}