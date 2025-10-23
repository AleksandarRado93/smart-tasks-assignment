package com.example.smart.tasks.ui.screens.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.usecase.GetAllTasksUseCase
import com.example.smart.tasks.domain.usecase.GetTasksForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    getAllTasks: GetAllTasksUseCase,
    private val getTasksForDateUseCase: GetTasksForDateUseCase
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val tasksFlow = getAllTasks.invoke()

    val uiState: StateFlow<TaskListUiState> = combine(
        tasksFlow,
        _selectedDate
    ) { response, date ->
        when (response) {
            is Response.Loading -> TaskListUiState.Loading

            is Response.Success -> TaskListUiState.Success(
                selectedDate = date,
                tasks = getTasksForDateUseCase(date, response.data)
            )

            is Response.Error -> {
                val cachedTasks = response.data.orEmpty()

                if (cachedTasks.isNotEmpty()) {
                    TaskListUiState.Success(
                        selectedDate = date,
                        tasks = getTasksForDateUseCase(date, cachedTasks),
                        errorMessage = response.message
                    )
                } else {
                    TaskListUiState.Error(response.message)
                }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TaskListUiState.Loading
    )

    fun changeSelectedDate(offset: Long) {
        _selectedDate.value = _selectedDate.value.plusDays(offset)
    }

    sealed class TaskListUiState {
        object Loading : TaskListUiState()
        data class Success(
            val selectedDate: LocalDate,
            val tasks: List<Task>,
            val errorMessage: String = ""
        ) : TaskListUiState()

        data class Error(val message: String) : TaskListUiState()
    }
}