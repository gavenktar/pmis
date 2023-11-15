package by.bsuir.kirylarol.wolfquotes;

import android.app.Application;
import androidx.lifecycle.viewmodel.compose.viewModel
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepositoryImpl
import by.bsuir.kirylarol.wolfquotes.DAO.TaskDao
import by.bsuir.kirylarol.wolfquotes.Database.AppDatabase
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepository
import by.bsuir.kirylarol.wolftasks.DataSource.RoomTaskDataSource
import by.bsuir.kirylarol.wolftasks.DataSource.TaskDataSource
import by.bsuir.kirylarol.wolftasks.Screens.EditWindow.EditTaskViewModel
import by.bsuir.kirylarol.wolftasks.Screens.QuoteWindow.HomeViewModel
import org.koin.android.ext.koin.androidContext;
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin;
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import java.util.UUID

val databaseModule = module {
    single<AppDatabase> { AppDatabase(context = get()) }
    single<TaskDao> { get<AppDatabase>().taskDao() }
    single<TaskDataSource> { RoomTaskDataSource (get<TaskDao>() ) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }

}

val viewModule = module {
    viewModel<HomeViewModel>() {
        HomeViewModel(get())
    }
    viewModel<EditTaskViewModel>() { (id: UUID?) ->
        EditTaskViewModel(get(),id)
    }
}


val appModule = module {
    includes(databaseModule)
    includes(viewModule)
}

class WolfApplication : Application() {

    val logger = PrintLogger(Level.DEBUG)
    @Override
    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(logger);
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}
