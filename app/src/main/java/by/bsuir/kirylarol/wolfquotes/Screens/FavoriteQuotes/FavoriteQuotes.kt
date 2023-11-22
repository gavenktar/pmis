package by.bsuir.kirylarol.wolfquotes.Screens.FavoriteQuotes

import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import by.bsuir.kirylarol.destinations.EditTaskDestination
import by.bsuir.kirylarol.destinations.QuoteCardDestination
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepository
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog.QuoteDialog
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteSource
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import by.bsuir.kirylarol.wolftasks.Entity.Task
import by.bsuir.kirylarol.wolftasks.Entity.TaskItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID
import kotlin.random.Random


sealed interface QuoteState {
    data object Loading : QuoteState
    data object Empty : QuoteState
    data class DisplayingQuotes(val tasks: List<Quote>) : QuoteState
    data class Error(val e: Exception) : QuoteState

    data object DisplayingQuote : QuoteState

}

class FavoriteQuoteViewModel(
    private val repository: QuoteRepository
) : ViewModel() {
    private val loading = MutableStateFlow(false)
    private val dispQuote = MutableStateFlow(false)

    val state = combine(
        repository.getQuotes(),
        loading,
    ) { quotes, loading ->
        if (loading) QuoteState.Loading else QuoteState.DisplayingQuotes(quotes)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuoteState.Loading)

    fun onClickRemove(id: UUID) = viewModelScope.launch {
        loading.update { true }
        repository.delete(id)
        loading.update { false }
    }

}


@Destination
@Composable
fun QuotesWindow(
    navigator: DestinationsNavigator,
    viewModel: FavoriteQuoteViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    fun onCardClick(quote: Quote) {
        val initalState = QuoteViewState(
            title = quote.title,
            author = quote.author,
            quoteSource = QuoteSource.QuoteSourceDB(quote.id.toString()),
            isInFavorite = true,
            showSnackBar = false,
            snackBarMessage = null
        )

        navigator.navigate(
            QuoteCardDestination(initalState)
        )
    }

    QuoteContent(
        state = state,
        onRemove = viewModel::onClickRemove,
        onCardClick = ::onCardClick
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuoteContent(
    state: QuoteState,
    onRemove: (id: UUID) -> Unit,
    onCardClick: (quote: Quote) -> Unit
) {

    val errorText = stringResource(R.string.error);
    Scaffold() {
        when (state) {
            is QuoteState.DisplayingQuotes -> LazyColumn {
                items(state.tasks) { quote ->
                    QuoteCard(
                        quote = quote.title,
                        author = quote.author,
                        onDeleteClick = { onRemove(quote.id) },
                        onCardClick = { onCardClick(quote) }
                    )
                }
            }


            is QuoteState.Error -> Text(
                state.e.message ?: stringResource(id = R.string.error_message)
            )

            is QuoteState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .fillMaxSize(0.5f)
//                            .align(Alignment.Center)
//                    )
                }
            }

            else -> throw AssertionError()
        }
    }
}

@Composable
fun QuoteCard(
    quote: String,
    author: String,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCardClick();
                }
                .padding(16.dp)
        ) {
            Text(
                text = quote,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = "Author: $author",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun QuoteCardPreview() {
    QuoteCard(
        quote = "This is a sample quote.",
        author = "John Doe",
        onDeleteClick = {},
        onCardClick = {},
    )
}


