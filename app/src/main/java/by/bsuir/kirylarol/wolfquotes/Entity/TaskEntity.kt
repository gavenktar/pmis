package by.bsuir.kirylarol.wolfquotes.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDate
import java.util.UUID


@Entity(tableName = TaskEntity.TableName)
class TaskEntity (
    @PrimaryKey val id : UUID,
    val title : String,
    val description : String,
    val dateStart : Long,
    val dateEnd : Long,
    val completed : Boolean

){
    internal companion object {
        const val TableName = "tasks" // <- удобно потом ссылаться будет
    }
}