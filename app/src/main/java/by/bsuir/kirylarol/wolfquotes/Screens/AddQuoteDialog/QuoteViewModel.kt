package by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.kirylarol.wolfquotes.API.QuoteService
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


sealed interface QuoteDialogState {
    data object Loading : QuoteDialogState
    data class Error(val e: Exception) : QuoteDialogState
    data class DisplayingQuote(
        val title: String,
        val author: String,
        val id: UUID?
    ) :
        QuoteDialogState
}

class ShowQuoteViewModel(
    private val repo: QuoteRepository,
    private val quoteService: QuoteService,
    private val id: UUID?
) : ViewModel() {

    private val saved = MutableStateFlow(false)
    private val loading = MutableStateFlow(false)

    private val quote = MutableStateFlow<Quote?>(null)

    fun init() {
        viewModelScope.launch {
            loading.value = true
            try {
                quote.update {
                    quoteService.getQuote()
                }
            } catch (e: Exception) {

            } finally {
                loading.value = false
            }
        }
    }
    val state = combine(
        quote,
        loading,
    ) { quote, loading ->
        if (loading) QuoteDialogState.Loading
        else {
            if (quote == null)
                QuoteDialogState.Error(RuntimeException("timed out"))
            else QuoteDialogState.DisplayingQuote(
                quote?.title ?: "",
                quote?.author ?: "",
                quote?.id
            )
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuoteDialogState.Loading)

    fun onClickAdd(
        title: String,
        author: String,
        id: UUID?
    ) = viewModelScope.launch {
        loading.update { true }
        repo.upsert(
            Quote(
                title,
                author,
                id ?: UUID.randomUUID()
            )
        )
        loading.update { false }
        saved.update { true }
    }
}