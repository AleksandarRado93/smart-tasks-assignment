package com.example.smart.tasks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smart.tasks.R
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.ui.theme.*

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (String) -> Unit
) {
    val statusColor = when (task.status) {
        TaskStatus.RESOLVED -> GreenResolved
        TaskStatus.CANT_RESOLVE -> RedCantResolve
        TaskStatus.UNRESOLVED -> OrangeUnresolved
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onTaskClick(task.id) },
        colors = CardDefaults.cardColors(containerColor = WhiteCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium.copy(color = statusColor),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.due_date_label),
                        style = MaterialTheme.typography.bodySmall.copy(color = TextGray)
                    )

                    Text(
                        text = task.dueDate ?: stringResource(R.string.no_due_date),
                        style = MaterialTheme.typography.bodyMedium.copy(color = statusColor)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.days_left_label),
                        style = MaterialTheme.typography.bodySmall.copy(color = TextGray)
                    )

                    Text(
                        text =
                            if (task.daysLeft < 0) stringResource(R.string.expired)
                            else task.daysLeft.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(color = statusColor)
                    )
                }
            }
        }
    }
}