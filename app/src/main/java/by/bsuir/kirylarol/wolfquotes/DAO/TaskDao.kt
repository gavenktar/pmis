package by.bsuir.kirylarol.wolfquotes.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import by.bsuir.kirylarol.wolfquotes.Entity.TaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
internal interface TaskDao {
    @Query("SELECT * FROM ${TaskEntity.TableName}")
    fun getAll(): Flow<List<TaskEntity>>

    @Upsert
    suspend fun save(e: TaskEntity)

    @Delete
    suspend fun delete (e: TaskEntity)
}