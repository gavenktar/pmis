package by.bsuir.kirylarol.wolfquotes.Screens.EditQuoteScreen

import androidx.compose.runtime.Composable
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.MVI.MVIViewModel
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class EditQuoteWindowState(
    var title: String,
    var author: String,
    var id: String,
    var confirmTaked: Boolean,
    var showConfirmWindow: Boolean
)

sealed interface EditQuoteWindowIntent {
    data object ClickSave : EditQuoteWindowIntent
    data object GoBack : EditQuoteWindowIntent
    data class ChangeQuote(val newQuote: String) : EditQuoteWindowIntent
    data class ChangeAuthor(val newAuthor: String) : EditQuoteWindowIntent
    data object ConfirmExit : EditQuoteWindowIntent
    data object CloseExitWindow : EditQuoteWindowIntent

}

sealed interface EditQuoteWindowAction {
    data class Save(val quote: Quote) : EditQuoteWindowAction
    data object GoBack : EditQuoteWindowAction
    data class ChangeQuote(val newQuote: String) : EditQuoteWindowAction
    data class ChangeAuthor(val newAuthor: String) : EditQuoteWindowAction
    data object ShowConfirmWindow : EditQuoteWindowAction
    data object HideConfirmWindow : EditQuoteWindowAction
}


class EditQuoteViewModel(
    initial: EditQuoteWindowState,
    val repository: QuoteRepository
) : MVIViewModel<EditQuoteWindowState, EditQuoteWindowIntent, EditQuoteWindowAction>(initial) {

    override suspend fun reduce(intent: EditQuoteWindowIntent) {
        when (intent) {
            is EditQuoteWindowIntent.ChangeQuote -> {
                state {
                    copy(title = intent.newQuote, confirmTaked = false)
                }
                event(EditQuoteWindowAction.ChangeQuote(intent.newQuote))
            }

            is EditQuoteWindowIntent.ChangeAuthor -> {
                state {
                    copy(author = intent.newAuthor, confirmTaked = false)
                }
                event(EditQuoteWindowAction.ChangeAuthor(intent.newAuthor))
            }

            is EditQuoteWindowIntent.ClickSave -> {
                repository.upsert(
                    Quote(
                        title = states.value.title,
                        author = states.value.author,
                        id = UUID.fromString(states.value.id)
                    )
                )
                delay(300L)
                event(
                    EditQuoteWindowAction.Save(
                        Quote(
                            title = states.value.title,
                            author = states.value.author,
                            id = UUID.fromString(states.value.id)
                        )
                    )
                )
                event(EditQuoteWindowAction.GoBack)
            }

            is EditQuoteWindowIntent.GoBack -> {
                if (states.value.confirmTaked == false) {

                    state{
                        copy(showConfirmWindow = true)
                    }
                    event(EditQuoteWindowAction.ShowConfirmWindow)
                } else {
                    event(EditQuoteWindowAction.GoBack)
                }
            }

            is EditQuoteWindowIntent.ConfirmExit -> {
                state {
                    copy(confirmTaked = true, showConfirmWindow = false)
                }
                event(EditQuoteWindowAction.GoBack)
            }

            is EditQuoteWindowIntent.CloseExitWindow -> {
                state {
                    copy(confirmTaked = false, showConfirmWindow = false)
                }
                event(EditQuoteWindowAction.HideConfirmWindow)
            }

            else -> {}
        }
    }
}
