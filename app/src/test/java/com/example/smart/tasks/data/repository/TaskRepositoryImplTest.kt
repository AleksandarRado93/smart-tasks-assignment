import app.cash.turbine.test
import com.example.smart.tasks.data.local.dao.TaskDao
import com.example.smart.tasks.data.local.model.TaskEntity
import com.example.smart.tasks.data.mapper.toDomain
import com.example.smart.tasks.data.mapper.toEntity
import com.example.smart.tasks.data.remote.api.ApiService
import com.example.smart.tasks.domain.response.Response
import com.example.smart.tasks.data.remote.dto.TaskDto
import com.example.smart.tasks.data.remote.dto.TasksResponse
import com.example.smart.tasks.data.repository.TaskRepositoryImpl
import com.example.smart.tasks.domain.model.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {

    private lateinit var repository: TaskRepositoryImpl
    private val mockApi = mock(ApiService::class.java)
    private val mockDao = mock(TaskDao::class.java)

    private fun makeTaskEntity(id: String = "1") = TaskEntity(
        id = id,
        targetDate = "2025-10-21",
        dueDate = null,
        title = "Test Task",
        description = null,
        priority = 0,
        status = 0,
        comment = null
    )

    private fun makeTaskDto(id: String = "1") = TaskDto(
        id = id,
        targetDate = "2025-10-21",
        dueDate = null,
        title = "Test Task",
        description = null,
        priority = 0
    )

    @Before
    fun setup() {
        repository = TaskRepositoryImpl(mockApi, mockDao)
    }

    @Test
    fun `getTasks emits cached and updated Success when API succeeds`() = runTest {
        val cached = listOf(makeTaskEntity())
        val remote = listOf(makeTaskDto())

        whenever(mockDao.getAllTasks()).thenReturn(flowOf(cached))
        whenever(mockDao.insertTasks(any())).thenReturn(Unit)
        whenever(mockApi.getTasks()).thenReturn(TasksResponse(remote))

        repository.getTasks().test {
            val first = awaitItem()
            assertTrue(first is Response.Success)
            assertEquals(cached.map { it.toDomain() }, (first as Response.Success).data)

            val second = awaitItem()
            assertTrue(second is Response.Success)
            assertEquals(remote.map { it.toEntity().toDomain() }, (second as Response.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTasks emits Error with cached data when API fails`() = runTest {
        val cached = listOf(makeTaskEntity())

        whenever(mockDao.getAllTasks()).thenReturn(flowOf(cached))
        whenever(mockApi.getTasks()).thenThrow(RuntimeException("API failed"))

        repository.getTasks().test {
            val first = awaitItem()
            assertTrue(first is Response.Success)
            assertEquals(cached.map { it.toDomain() }, (first as Response.Success).data)

            val second = awaitItem()
            assertTrue(second is Response.Error)
            val errorState = second as Response.Error
            assertEquals("API failed", errorState.message)
            assertEquals(cached.map { it.toDomain() }, errorState.data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTasks emits Error with empty data when API fails and no cached tasks`() = runTest {
        whenever(mockDao.getAllTasks()).thenReturn(flowOf(emptyList()))
        whenever(mockApi.getTasks()).thenThrow(RuntimeException("API failed"))

        repository.getTasks().test {
            val first = awaitItem()
            assertTrue(first is Response.Success)
            assertEquals(emptyList<Task>(), (first as Response.Success).data)

            val second = awaitItem()
            assertTrue(second is Response.Error)
            val errorState = second as Response.Error
            assertEquals("API failed", errorState.message)
            assertEquals(emptyList<Task>(), errorState.data)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
