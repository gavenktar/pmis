package by.bsuir.kirylarol.wolftasks.DataSource

import by.bsuir.kirylarol.wolfquotes.DAO.TaskDao
import by.bsuir.kirylarol.wolfquotes.Entity.TaskEntity
import by.bsuir.kirylarol.wolftasks.Entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

interface TaskDataSource {
    fun getTasks(): Flow<List<Task>>
    fun getTask(id: UUID): Flow<Task?>

    suspend fun upsert(task: Task)
    suspend fun delete(id: UUID)
    suspend fun setDone(id: UUID)

}

internal class RoomTaskDataSource(private val dao: TaskDao) : TaskDataSource {


    private val _tasksFlow = MutableSharedFlow<Map<UUID, Task>>()


    private lateinit var tasks: MutableMap<UUID, Task>;

         init {
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAll().collect { taskEntities ->
                tasks = taskEntities.associateBy { it.id }
                    .mapValues { entry ->
                        Task(
                            id = entry.value.id,
                            title = entry.value.title,
                            description = entry.value.description,
                            dateCreated = LocalDate.ofEpochDay(entry.value.dateStart),
                            dateEnd = LocalDate.ofEpochDay(entry.value.dateEnd),
                            completed = entry.value.completed
                        )
                    }
                    .toMutableMap()
            }
        }
    }


    override fun getTask(id: UUID): Flow<Task?> = _tasksFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(tasks)
        }
        .map { it[id] }


    private fun toTaskEntity(task: Task): TaskEntity {
        return TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            dateStart = task.dateCreated.toEpochDay() ,
            dateEnd = task.dateEnd.toEpochDay(),
            completed = task.completed
        )
    }

    override fun getTasks(): Flow<List<Task>> = _tasksFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(tasks)
        }
        .map { it.values.toList() }

    override suspend fun upsert(task: Task) {
        dao.save(toTaskEntity(task))
    }

    override suspend fun delete(id: UUID) {
        val task = tasks[id]
        if (task != null) {
            dao.delete(toTaskEntity(task))
            tasks.remove(id)
            _tasksFlow.emit(tasks)
        }
    }

    override suspend fun setDone(id: UUID) {
        var task = tasks[id];
        if (task != null) {
            task.completed = !task.completed

            tasks[id] = task;
            dao.save(toTaskEntity(task))
        }
    }
}


object InMemoryDataSource : TaskDataSource {

    private val DefaultTasks = setOf(
        Task(
            "Сделать сто подтягиваний", "Все понятно?", LocalDate.of(2023, 6, 6),
            LocalDate.of(2023, 7, 6), true
        ),
        Task(
            "Сделать двесте подтягиваний",
            "Пора ставить себе новые рекорды",
            LocalDate.of(2023, 10, 8),
            LocalDate.of(2023, 7, 6),
            false
        ),
    )

    private val tasks = DefaultTasks.associateBy { it.id }.toMutableMap()

    private val _tasksFlow = MutableSharedFlow<Map<UUID, Task>>()

    override fun getTask(id: UUID): Flow<Task?> = _tasksFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(tasks)
        }
        .map { it[id] }

    override fun getTasks(): Flow<List<Task>> = _tasksFlow
        .asSharedFlow()
        .onStart {
            delay(1000L)
            emit(tasks)
        }
        .map { it.values.toList() }

    override suspend fun upsert(task: Task) {
        delay(1000L)
        tasks[task.id] = task
        _tasksFlow.emit(tasks)
    }

    override suspend fun delete(id: UUID) {
        delay(1000L)
        tasks.remove(id)
        _tasksFlow.emit(tasks)
    }

    override suspend fun setDone(id: UUID) {
        delay(100L)
        var task = tasks[id];
        if (task != null) {
            task.completed = !task.completed

            tasks[id] = task;
        }
        _tasksFlow.emit(tasks)
    }
}