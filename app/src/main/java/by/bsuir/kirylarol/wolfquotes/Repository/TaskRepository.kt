package by.bsuir.kirylarol.wolfquotes.Repository

import by.bsuir.kirylarol.wolftasks.DataSource.TaskDataSource
import by.bsuir.kirylarol.wolftasks.Entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

interface TaskRepository {

    fun getTasks(): Flow<List<Task>>
    fun getTask(id: UUID?): Flow<Task?>

    suspend fun upsert(Task: Task)

    suspend fun delete(id: UUID)
    suspend fun setDone (id: UUID)
}

class TaskRepositoryImpl (
    private val dataSource: TaskDataSource
) : TaskRepository{

    override fun getTasks(): Flow<List<Task>> = dataSource.getTasks()
    override fun getTask(id: UUID?): Flow<Task?> = id?.let { dataSource.getTask(it) } ?: flowOf(null)

    override suspend fun upsert(Task: Task) = dataSource.upsert(Task)
    override suspend fun delete(id: UUID) = dataSource.delete(id)

    override suspend fun setDone(id: UUID) {
        dataSource.setDone (id)
    }

}
