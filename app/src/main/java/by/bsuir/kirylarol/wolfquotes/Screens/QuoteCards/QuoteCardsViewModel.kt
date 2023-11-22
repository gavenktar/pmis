package by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards

import androidx.room.TypeConverters
import by.bsuir.kirylarol.wolfquotes.Database.UUIDConverter
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.MVI.MVIViewModel
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.Serializable

import java.util.UUID

@Serializable
sealed class QuoteSource {
    @Serializable
    data class QuoteSourceDB(val id: String) : QuoteSource()
    @Serializable
    data object QuoteSourceInternet : QuoteSource()
}


@Serializable
class QuoteViewState(
    val title: String = "",
    val author: String = "",
    val quoteSource: QuoteSource,
    val isInFavorite: Boolean,
    val showSnackBar : Boolean,
    val snackBarMessage : String?,
)

sealed interface QuoteViewModelIntent {

    data class ClickRedact(val quote: Quote) : QuoteViewModelIntent
    data object ClickClose : QuoteViewModelIntent
    data class ClickAddToFavorite(val quote: Quote) : QuoteViewModelIntent
    data class ClickRemoveFromFavorite(val id: UUID?) : QuoteViewModelIntent


}

sealed interface QuoteViewModelAction {

    data class GoRedact(val quote: Quote) : QuoteViewModelAction
    data object GoBack : QuoteViewModelAction
    data class AddToFavorite(val quote: Quote) : QuoteViewModelAction
    data class RemoveFromFavorite(val id: UUID?) : QuoteViewModelAction
    data object ShowSnackbar : QuoteViewModelAction
}


class QuoteViewModel(
    initial: QuoteViewState,
    val repository: QuoteRepository
) : MVIViewModel<QuoteViewState, QuoteViewModelIntent, QuoteViewModelAction>(initial) {

    val _state = MutableStateFlow<QuoteViewState>(initial)

    private val _snackbarEvent = MutableSharedFlow<String?>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()



    override suspend fun reduce(intent: QuoteViewModelIntent) {
        when (intent) {
            is QuoteViewModelIntent.ClickClose -> {
                println("Закрыто")
                event(QuoteViewModelAction.GoBack)
            }

            is QuoteViewModelIntent.ClickRedact -> {
                println("Редакт")
                event(QuoteViewModelAction.GoRedact(intent.quote))
            }

            is QuoteViewModelIntent.ClickAddToFavorite -> {
                println("В фаворите")
                val quote = Quote(
                    title = _state.value.title,
                    author = _state.value.author,
                )
                val updatedState = QuoteViewState(
                    title = _state.value.title,
                    author = _state.value.author,
                    quoteSource = QuoteSource.QuoteSourceDB(quote.id.toString()),
                    isInFavorite = true,
                    showSnackBar = true,
                    snackBarMessage = "Добавлено в любимые",
                )
                repository.upsert(
                    quote
                )
                state {
                    updatedState
                }
                event(QuoteViewModelAction.ShowSnackbar)
                event(QuoteViewModelAction.AddToFavorite(intent.quote))
            }

            is QuoteViewModelIntent.ClickRemoveFromFavorite -> {
                println("Ремуве фаворите")
                if (intent.id != null) {
                    repository.delete(intent.id);
                }
                val updatedState = QuoteViewState(
                    title = _state.value.title,
                    author = _state.value.author,
                    quoteSource = QuoteSource.QuoteSourceInternet,
                    isInFavorite = false,
                    showSnackBar = true,
                    snackBarMessage = "Удалено с любимых",
                    )
                state {
                    updatedState
                }
                event(QuoteViewModelAction.ShowSnackbar)
                event(QuoteViewModelAction.RemoveFromFavorite(intent.id))
            }
        }
    }
}

