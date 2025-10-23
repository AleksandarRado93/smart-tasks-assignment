package com.example.smart.tasks.ui.screens.task_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.domain.usecase.GetTaskByIdUseCase
import com.example.smart.tasks.domain.usecase.UpdateTaskStatusAndCommentUseCase
import com.example.smart.tasks.ui.navigation.Args
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    getTaskByIdUseCase: GetTaskByIdUseCase,
    savedStateHandle: SavedStateHandle,
    private val updateTaskStatusAndCommentUseCase: UpdateTaskStatusAndCommentUseCase
) : ViewModel() {

    private val taskId: String = checkNotNull(savedStateHandle[Args.TASK_ID])

    val uiState: StateFlow<TaskDetailsUiState> =
        getTaskByIdUseCase(taskId)
            .map { task ->
                if (task != null) TaskDetailsUiState.Success(task)
                else TaskDetailsUiState.Error("Task not found")
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                TaskDetailsUiState.Loading
            )

    fun updateTaskStatusAndComment(status: TaskStatus, comment: String?) {
        viewModelScope.launch {
            updateTaskStatusAndCommentUseCase.invoke(taskId, status, comment)
        }
    }

    sealed class TaskDetailsUiState {
        object Loading : TaskDetailsUiState()
        data class Success(val task: Task) : TaskDetailsUiState()
        data class Error(val message: String) : TaskDetailsUiState()
    }
}