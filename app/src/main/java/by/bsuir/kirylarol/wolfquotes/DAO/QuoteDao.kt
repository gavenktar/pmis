package by.bsuir.kirylarol.wolfquotes.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import by.bsuir.kirylarol.wolfquotes.Entity.QuoteEntity
import by.bsuir.kirylarol.wolfquotes.Entity.TaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
internal interface QuoteDao {
    @Query("SELECT * FROM ${QuoteEntity.TableName}")
    fun getAll(): Flow<List<QuoteEntity>>

    @Upsert
    suspend fun save(e: QuoteEntity)

    @Delete
    suspend fun delete (e: QuoteEntity)
}