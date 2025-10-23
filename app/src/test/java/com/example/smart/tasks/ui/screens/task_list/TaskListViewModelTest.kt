package com.example.smart.tasks.ui.screens.task_list

import app.cash.turbine.test
import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.domain.model.Task
import com.example.smart.tasks.domain.model.TaskStatus
import com.example.smart.tasks.domain.usecase.GetAllTasksUseCase
import com.example.smart.tasks.domain.usecase.GetTasksForDateUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var getTasksForDateUseCase: GetTasksForDateUseCase
    private lateinit var viewModel: TaskListViewModel

    private val today = LocalDate.of(2025, 10, 21)

    @Before
    fun setup() {
        getAllTasksUseCase = mock()
        getTasksForDateUseCase = mock()
    }

    @Test
    fun `uiState emits Loading as first state`() = runTest {
        viewModel = TaskListViewModel(getAllTasksUseCase, getTasksForDateUseCase)

        viewModel.uiState.test {
            val firstState = awaitItem()
            assertTrue(firstState is TaskListViewModel.TaskListUiState.Loading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Success when repository returns Success`() = runTest {
        val tasks = listOf(makeTask())

        whenever(getAllTasksUseCase.invoke())
            .thenReturn(flowOf(Response.Success(tasks)))
        whenever(getTasksForDateUseCase.invoke(today, tasks))
            .thenReturn(tasks)

        viewModel = TaskListViewModel(getAllTasksUseCase, getTasksForDateUseCase)

        viewModel.uiState.test {
            val firstState = awaitItem()
            assertTrue(firstState is TaskListViewModel.TaskListUiState.Loading)

            val secondState = awaitItem()
            assertTrue(secondState is TaskListViewModel.TaskListUiState.Success)

            val successState = secondState as TaskListViewModel.TaskListUiState.Success
            assertEquals(listOf(makeTask()), successState.tasks)
            assertEquals(today, successState.selectedDate)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Success when repository returns Error but there is cached tasks`() =
        runTest {
            val tasks = listOf(makeTask())

            whenever(getAllTasksUseCase.invoke())
                .thenReturn(flowOf(Response.Error(ERROR_MSG, tasks)))
            whenever(getTasksForDateUseCase.invoke(today, tasks))
                .thenReturn(tasks)

            viewModel = TaskListViewModel(getAllTasksUseCase, getTasksForDateUseCase)

            viewModel.uiState.test {
                val firstState = awaitItem()
                assertTrue(firstState is TaskListViewModel.TaskListUiState.Loading)

                val secondState = awaitItem()
                assertTrue(secondState is TaskListViewModel.TaskListUiState.Success)

                val successState = secondState as TaskListViewModel.TaskListUiState.Success
                assertEquals(listOf(makeTask()), successState.tasks)
                assertEquals(ERROR_MSG, successState.errorMessage)
                assertEquals(today, successState.selectedDate)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `uiState emits Error when repository returns Error and no cached tasks`() = runTest {
        whenever(getAllTasksUseCase.invoke())
            .thenReturn(flowOf(Response.Error(ERROR_MSG, data = emptyList())))

        viewModel = TaskListViewModel(getAllTasksUseCase, getTasksForDateUseCase)

        viewModel.uiState.test {
            val firstState = awaitItem()
            assertTrue(firstState is TaskListViewModel.TaskListUiState.Loading)

            val secondState = awaitItem()
            assertTrue(secondState is TaskListViewModel.TaskListUiState.Error)

            val errorState = secondState as TaskListViewModel.TaskListUiState.Error
            assertEquals(ERROR_MSG, errorState.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private fun makeTask(
            id: String = "1",
            title: String = "Task",
            targetDate: String = "2025-10-21",
            dueDate: String? = null,
            priority: Int = 0,
            status: TaskStatus = TaskStatus.UNRESOLVED,
            description: String? = null,
            comment: String? = null
        ) = Task(id, targetDate, dueDate, title, description, priority, status, comment)

        private const val ERROR_MSG = "GENERIC ERROR"
    }
}