package by.bsuir.kirylarol.wolftasks.Screens.InfoWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.kirylarol.destinations.EditQuoteWindowDestination
import by.bsuir.kirylarol.wolfquotes.DataSource.QuoteDataSource
import by.bsuir.kirylarol.wolfquotes.Entity.Quote
import by.bsuir.kirylarol.wolfquotes.MVI.Container
import by.bsuir.kirylarol.wolfquotes.MVI.subscribe
import by.bsuir.kirylarol.wolfquotes.Repository.QuoteRepository
import by.bsuir.kirylarol.wolfquotes.Screens.EditQuoteScreen.EditQuoteWindowState
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteSource
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewModelAction
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewModelIntent
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.getScopeId
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.util.UUID


@Composable
inline fun <reified T, S, I, E> container(
    // 2
    noinline params: ParametersDefinition? = null,
): Container<S, I, E> where T : Container<S, I, E>, T : ViewModel =
    getViewModel<T>(parameters = params)


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun QuoteCard(
    quote: QuoteViewState?,
    navigator: DestinationsNavigator = EmptyDestinationsNavigator,
) {
    var currentMessage: String = ""
    val container = container<QuoteViewModel, _, _, _> { parametersOf(quote) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState();
    }
    container.subscribe(onEvent = { event ->
        when (event) {
            is QuoteViewModelAction.GoBack -> {
                navigator.navigateUp()
            }

            is QuoteViewModelAction.GoRedact -> {

                if (quote != null) {
                    var idValue: String = ""
                    when (quote.quoteSource) {
                        is QuoteSource.QuoteSourceDB -> {
                            idValue = quote.quoteSource.id
                        }

                        is QuoteSource.QuoteSourceInternet -> {
                            println("Это интернет-цитата")
                        }
                    }

                    val initalState = EditQuoteWindowState(
                        title = quote.title,
                        author = quote.author,
                        id = idValue,
                        confirmTaked = true,
                        showConfirmWindow = false
                    )
                    navigator.navigate(EditQuoteWindowDestination(initalState))
                }
            }

            is QuoteViewModelAction.ShowSnackbar -> {
                if (container.states.value.snackBarMessage != null) {
                    val name = container.states.value.snackBarMessage;
                    if (name != currentMessage && name != null) {
                        currentMessage = name;
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                name
                            )
                        }
                    }
                }
            }

            else -> {}
        }
    })
    QuoteCardContent(
        state = container.states.collectAsStateWithLifecycle(
        ).value,
        snackbarHostState
    ) { intent ->
        container.intent(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteCardContent(
    state: QuoteViewState,
    snackBarHostState: SnackbarHostState,
    intent: (QuoteViewModelIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
                .shadow(16.dp, shape = MaterialTheme.shapes.medium),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = state.author,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = SpaceBetween
                ) {

                    if (state.quoteSource != QuoteSource.QuoteSourceInternet) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    intent(
                                        QuoteViewModelIntent.ClickRedact(
                                            Quote(
                                                state.title,
                                                state.author,
                                            )
                                        )
                                    )
                                }
                        )
                    }
                    if (!state.isInFavorite) {
                        IconButton(
                            onClick = {
                                intent(
                                    QuoteViewModelIntent.ClickAddToFavorite(
                                        Quote(
                                            state.title,
                                            state.author,
                                        )
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                            )
                        }
                    } else {
                        var id: String = "";
                        when (state.quoteSource) {
                            is QuoteSource.QuoteSourceDB -> {
                                id = state.quoteSource.id
                            }

                            is QuoteSource.QuoteSourceInternet -> {
                            }
                        }
                        IconButton(
                            onClick = {
                                intent(
                                    QuoteViewModelIntent.ClickRemoveFromFavorite(
                                        UUID.fromString(id)
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }
    if (state.showSnackBar && state.snackBarMessage != null) {
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.fillMaxSize()
        ) { data ->
            Snackbar(
                snackbarData = data,
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun QuoteCardContentPreview() {
    val quote = QuoteViewState(
        title = "To be or not to be, that is the question.",
        author = "William Shakespeare",
        quoteSource = QuoteSource.QuoteSourceDB("хуйхуйхухй"),
        isInFavorite = false,
        showSnackBar = false,
        snackBarMessage = null
    )
//    QuoteCard(quote);
}



