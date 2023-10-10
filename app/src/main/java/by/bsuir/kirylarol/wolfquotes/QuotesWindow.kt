package by.bsuir.kirylarol.wolfquotes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import by.bsuir.kirylarol.wolfquotes.destinations.QuotesWindowDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.ui.Modifier
import by.bsuir.kirylarol.wolfquotes.destinations.EditQuoteDestination
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination
@Composable
fun QuotesWindow(
    navigator: DestinationsNavigator
){
    val viewModel = viewModel<QuoteViewModel>()
    QuotesContent(items = viewModel.items, onRemove = viewModel::onClickRemove, onAdd = viewModel::onClickAdd, onEdit = { navigator.navigate(EditQuoteDestination) }, onComplete = {}, onInfo = {}, navigator = navigator)
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuotesContent(
    items : List<Quote>,
    onAdd : (Quote) -> Unit,
    onInfo : (Quote) -> Unit,
    onComplete : (Quote) -> Unit,
    onEdit : (Quote) -> Unit,
    onRemove : (Quote) -> Unit,
    navigator: DestinationsNavigator
)
{
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { FloatingActionButton(onClick = {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Успешно добавлено",
                    actionLabel = "ОК",
                    duration = SnackbarDuration.Indefinite
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        /* Handle snackbar action performed */
                    }

                    SnackbarResult.Dismissed -> {
                        /* Handle snackbar dismissed */
                    }

                }
            }
            // navigator.navigate(EditQuoteDestination);
            onAdd(Quote());
        }){
            Icon(imageVector = Icons.Default.Add, contentDescription = "AddQuote", tint = MaterialTheme.colorScheme.primary)
        }},
    ){
        LazyColumn(
            Modifier.padding(5.dp)
        ){

            items(
                items = items,
                key = { quote ->
                    quote.id
                }
            ) { item ->
                QuoteItem(
                    quote = item,
                    modifier = Modifier
                        .heightIn(200.dp, 250.dp)
                        .padding(5.dp),
                    onInfo = onInfo,
                    onComplete = onComplete,
                    onEdit = onEdit,
                    onRemove = onRemove
                )
            }
        }
    }
}

@Preview
@Composable
fun QuotesWindowPreview(){
    QuotesWindow(navigator = EmptyDestinationsNavigator)
}