package by.bsuir.kirylarol.wolfquotes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import by.bsuir.kirylarol.wolfquotes.destinations.EditQuoteDestination
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    val errorText = stringResource(R.string.error);
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showError by remember { mutableStateOf(false)}
    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)
        },
            floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                println("in snackbar")
                if (Random.nextBoolean()) {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Успешно добавлено",
                            actionLabel = "ОК",
                            duration = SnackbarDuration.Indefinite
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                            }

                            SnackbarResult.Dismissed -> {

                            }

                        }
                        onAdd(Quote());
                        showError =false;
                    }
                }else{
                    showError = true;
                }
            // navigator.navigate(EditQuoteDestination);

        }){
            Icon(imageVector = Icons.Default.Add, contentDescription = "AddQuote", tint = MaterialTheme.colorScheme.primary)
        }},
    ){
        if (showError){
            SnackbarItem (
                Icons.Default.Close,
                errorText
            )
        }
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
                        .heightIn(200.dp, 220.dp)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarItem(
    imageVector: ImageVector,
    text : String,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var open by remember {
        mutableStateOf(true)
    }
    if (open) {
        ModalBottomSheet(
            onDismissRequest = {
                open = false;
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge,
                )
                Icon(
                    imageVector = imageVector,
                    contentDescription = text,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(3f),
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
                Button(onClick = {
                    open = false;
                }, modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f)
                    .align(CenterHorizontally),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OK",
                            modifier = Modifier.padding(5.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}