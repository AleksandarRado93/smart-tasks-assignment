package com.example.smart.tasks.ui.screens.task_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smart.tasks.R
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.ui.components.CommentDialog
import com.example.smart.tasks.ui.components.ErrorScreen
import com.example.smart.tasks.ui.components.LoadingScreen
import com.example.smart.tasks.ui.components.TaskDetailsCard
import com.example.smart.tasks.ui.theme.GreenResolved
import com.example.smart.tasks.ui.theme.RedCantResolve
import com.example.smart.tasks.ui.theme.WhiteCard
import com.example.smart.tasks.ui.theme.YellowBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    state: TaskDetailsViewModel.TaskDetailsUiState,
    onUpdateTaskStatusAndCommentClick: (TaskStatus, String?) -> Unit
) {
    when (state) {
        is TaskDetailsViewModel.TaskDetailsUiState.Loading -> LoadingScreen()

        is TaskDetailsViewModel.TaskDetailsUiState.Error -> ErrorScreen(state.message)

        is TaskDetailsViewModel.TaskDetailsUiState.Success -> {
            val task = state.task
            var showCommentDialog by remember { mutableStateOf<TaskStatus?>(null) }

            showCommentDialog?.let { status ->
                CommentDialog(
                    onDismiss = {
                        onUpdateTaskStatusAndCommentClick(status, null)
                        showCommentDialog = null
                    },
                    onConfirm = { comment ->
                        onUpdateTaskStatusAndCommentClick(status, comment)
                        showCommentDialog = null
                    }
                )
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.task_detail_title),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WhiteCard
                                )
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = YellowBackground
                        )
                    )
                },
                containerColor = YellowBackground
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(Modifier.height(16.dp))

                    TaskDetailsCard(task = task)

                    Spacer(Modifier.height(32.dp))

                    when (task.status) {
                        TaskStatus.RESOLVED -> {
                            Image(
                                painter = painterResource(R.drawable.ic_sign_resolved),
                                contentDescription = stringResource(R.string.status_resolved),
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        TaskStatus.CANT_RESOLVE -> {
                            Image(
                                painter = painterResource(R.drawable.unresolved_sign),
                                contentDescription = stringResource(R.string.status_cant_resolve),
                                modifier = Modifier.size(100.dp)
                            )
                        }

                        else -> {
                            Spacer(Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(horizontal = 24.dp)
                            ) {
                                Button(
                                    onClick = {
                                        showCommentDialog = TaskStatus.RESOLVED
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GreenResolved
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = stringResource(R.string.resolve_task),
                                        color = WhiteCard
                                    )
                                }

                                Button(
                                    onClick = {
                                        showCommentDialog = TaskStatus.CANT_RESOLVE
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RedCantResolve
                                    ),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = stringResource(R.string.cant_resolve_task),
                                        color = WhiteCard
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}