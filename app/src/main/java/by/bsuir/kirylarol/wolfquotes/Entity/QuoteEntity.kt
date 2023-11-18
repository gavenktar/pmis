package by.bsuir.kirylarol.wolfquotes.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = QuoteEntity.TableName)
class QuoteEntity (
    @PrimaryKey val id : UUID,
    val title : String,
    val author : String
){
    internal companion object {
        const val TableName = "quotes"
    }
}