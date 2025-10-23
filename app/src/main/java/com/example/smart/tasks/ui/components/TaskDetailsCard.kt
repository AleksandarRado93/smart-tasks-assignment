package com.example.smart.tasks.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart.tasks.R
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.ui.theme.*

@Composable
fun TaskDetailsCard(
    task: Task,
    modifier: Modifier = Modifier
) {
    val statusColor = when (task.status) {
        TaskStatus.RESOLVED -> GreenResolved
        TaskStatus.CANT_RESOLVE -> RedCantResolve
        TaskStatus.UNRESOLVED -> OrangeUnresolved
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_task_details_container),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 32.dp),
            colors = CardDefaults.cardColors(containerColor = WhiteCard),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            stringResource(R.string.due_date_label),
                            color = TextGray,
                            fontSize = 12.sp
                        )
                        Text(
                            task.dueDate ?: stringResource(R.string.no_due_date),
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            stringResource(R.string.days_left_label),
                            color = TextGray,
                            fontSize = 12.sp
                        )
                        Text(
                            if (task.daysLeft < 0) stringResource(R.string.expired)
                            else task.daysLeft.toString(),
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = task.description ?: stringResource(R.string.no_description),
                    color = TextDark,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (task.comment.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.comment_label),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(task.comment, color = TextDark)
                }

                Spacer(Modifier.height(12.dp))

                HorizontalDivider(color = DividerLight)

                Spacer(Modifier.height(8.dp))

                Text(
                    text = when (task.status) {
                        TaskStatus.RESOLVED -> stringResource(R.string.status_resolved)
                        TaskStatus.CANT_RESOLVE -> stringResource(R.string.status_cant_resolve)
                        TaskStatus.UNRESOLVED -> stringResource(R.string.status_unresolved)
                    },
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
