package by.bsuir.kirylarol.wolfquotes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bsuir.kirylarol.wolfquotes.DAO.TaskDao
import by.bsuir.kirylarol.wolfquotes.Entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 2)
@TypeConverters(UUIDConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

internal fun AppDatabase(context: Context) = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "task_db"
)
    .fallbackToDestructiveMigration()
    .build()