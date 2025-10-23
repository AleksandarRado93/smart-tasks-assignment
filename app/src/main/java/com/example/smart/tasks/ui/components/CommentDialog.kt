package com.example.smart.tasks.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.smart.tasks.R

@Composable
fun CommentDialog(
    onConfirm: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.leave_comment_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text(stringResource(R.string.comment_label)) },
                    placeholder = { Text(stringResource(R.string.comment_hint)) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(comment.ifBlank { null }) }) {
                Text(stringResource(R.string.confirm_comment))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}