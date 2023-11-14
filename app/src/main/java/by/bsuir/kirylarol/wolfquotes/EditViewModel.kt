package by.bsuir.kirylarol.wolfquotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import java.util.UUID
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


sealed interface EditViewState {
    data object Loading : EditViewState
    data class Error(val e: Exception) : EditViewState
    data class EditingQuote(val title: String,
                            val description: String,
                            val dateCreated: LocalDate,
                            val dateEnd: LocalDate,
                            var completed: Boolean,
                            val id: UUID? = null, val saved: Boolean = false) :
        EditViewState
}

class EditQuoteViewModel(
    private val id: UUID?,
    private val repo: QuoteRepository = QuoteRepositoryImpl,
) : ViewModel() {

    private val saved = MutableStateFlow(false)
    private val loading = MutableStateFlow(false)

    val state = combine(
        repo.getQuote(id),
        saved,
        loading,
    ) { quote, saved, loading ->
        if (loading) EditViewState.Loading else EditViewState.EditingQuote(
            quote?.title ?: "",
            quote?.description ?: "",
            quote?.dateCreated ?: LocalDate.now(),
            quote?.dateEnd ?: LocalDate.now(),
            quote?.completed?: false,
            quote?.id,
            saved
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), EditViewState.Loading)

    fun onClickSave(
        title : String,
        description : String,
        dateCreated : LocalDate,
        dateEnd : LocalDate,
        completed : Boolean,
        id : UUID?
    ) = viewModelScope.launch {
        loading.update { true }
        repo.upsert(Quote(title, description, dateCreated, dateEnd, completed, id ?: UUID.randomUUID()))
        loading.update { false }
        saved.update { true }
    }
}
