package by.bsuir.kirylarol.wolfquotes;

import android.app.Application;
import by.bsuir.kirylarol.wolfquotes.API.QuoteHttpClient
import by.bsuir.kirylarol.wolfquotes.API.QuoteService
import by.bsuir.kirylarol.wolfquotes.API.QuoteServiceImpl
import by.bsuir.kirylarol.wolfquotes.DAO.QuoteDao
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepositoryImpl
import by.bsuir.kirylarol.wolfquotes.DAO.TaskDao
import by.bsuir.kirylarol.wolfquotes.DataSource.QuoteDataSource
import by.bsuir.kirylarol.wolfquotes.DataSource.RoomQuoteDataSource
import by.bsuir.kirylarol.wolfquotes.Database.AppDatabase
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepositoryImpl
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepository
import by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog.ShowQuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.EditQuoteScreen.EditQuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.EditQuoteScreen.EditQuoteWindowState
import by.bsuir.kirylarol.wolfquotes.Screens.FavoriteQuotes.FavoriteQuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import by.bsuir.kirylarol.wolftasks.DataSource.RoomTaskDataSource
import by.bsuir.kirylarol.wolftasks.DataSource.TaskDataSource
import by.bsuir.kirylarol.wolftasks.Screens.EditWindow.EditTaskViewModel
import by.bsuir.kirylarol.wolftasks.Screens.QuoteWindow.HomeViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext;
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin;
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.core.scope.get
import org.koin.dsl.module
import java.util.UUID

val databaseModule = module {
    single<AppDatabase> { AppDatabase(context = get()) }
    single<TaskDao> { get<AppDatabase>().taskDao() }
    single<QuoteDao> { get<AppDatabase>().quoteDao() }
    single<TaskDataSource> { RoomTaskDataSource(get<TaskDao>()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<QuoteDataSource> {
        RoomQuoteDataSource(get<AppDatabase>().quoteDao())
    }
    single<QuoteService> { QuoteServiceImpl(get()) }
    single<QuoteRepository> { QuoteRepositoryImpl(get()) }
}

val viewModule = module {
    viewModel<HomeViewModel>() {
        HomeViewModel(get())
    }
    viewModel<EditTaskViewModel>() { (id: UUID?) ->
        EditTaskViewModel(get(), id)
    }
    viewModel<ShowQuoteViewModel> {
        ShowQuoteViewModel(get<QuoteRepository>(), get<QuoteService>(), null)
    }

    viewModel<FavoriteQuoteViewModel> { FavoriteQuoteViewModel(get()) }
    viewModel<QuoteViewModel> { (initial: QuoteViewState) ->
        QuoteViewModel(initial,get<QuoteRepository>())
    }
}


val editQuoteModule = module {
    viewModel<EditQuoteViewModel>(){
            (initial: EditQuoteWindowState) ->
        EditQuoteViewModel(initial,get<QuoteRepository>())
    }
}


val networkModule = module {

    single<HttpClient> { QuoteHttpClient() }
    single<QuoteService> {
        QuoteServiceImpl(get<HttpClient>())
    }
}


val appModule = module {
    includes(networkModule)
    includes(databaseModule)
    includes(viewModule)
    includes(editQuoteModule)
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
