package by.bsuir.kirylarol.wolftasks.Screens.QuoteWindow

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import by.bsuir.kirylarol.wolfquotes.Repository.TaskRepository
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog.QuoteDialog
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


sealed interface HomeState {
    data object Loading : HomeState
    data object Empty : HomeState
    data class DisplayingTasks(val tasks: List<Task>) : HomeState
    data class Error(val e: Exception) : HomeState

    data object DisplayingQuote : HomeState

}

class HomeViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val loading = MutableStateFlow(false)
    private val dispQuote = MutableStateFlow(false)

    val state = combine(
        repository.getTasks(),
        loading,
        dispQuote
    ) { tasks, loading, dispQuote ->
            if (dispQuote) HomeState.DisplayingQuote
            else if (loading) HomeState.Loading else HomeState.DisplayingTasks(tasks)
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), HomeState.Loading)

    fun onClickRemove(id: UUID) = viewModelScope.launch {
        loading.update { true }
        repository.delete(id)
        loading.update { false }
    }

    fun onClickDone(id: UUID) = viewModelScope.launch {
        loading.update { true }
        dispQuote.update { true }
        repository.setDone(id)
        loading.update { false }
    }

    fun closeQuoteWindow() {
        dispQuote.update { false }
    }

}


@RootNavGraph(start = true)
@Destination
@Composable
fun TasksWindow(
    navigator: DestinationsNavigator,
    viewModel : HomeViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    TasksContent(
        state = state,
        onEdit = {
            navigator.navigate(EditTaskDestination(it, true))
        },
        onInfo = {
            navigator.navigate(EditTaskDestination(it, false))
        },
        onRemove = viewModel::onClickRemove,
        onDone = viewModel::onClickDone,
        closeQuoteWindow = viewModel::closeQuoteWindow
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TasksContent(
    state: HomeState,
    onRemove: (id: UUID) -> Unit,
    onEdit: (id: UUID?) -> Unit,
    onInfo: (id: UUID?) -> Unit,
    onDone: (id: UUID) -> Unit,
    closeQuoteWindow : () -> Unit
) {

    val errorText = stringResource(R.string.error);
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                println("in snackbar")
                if (Random.nextBoolean()) {
                    onEdit(null)
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Успешно добавлено",
                            actionLabel = "ОК",
                            duration = SnackbarDuration.Indefinite
                        )
                        showError = false;

                    }
                } else {
                    showError = true;
                }

            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "AddTask",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    ) {
        if (showError) {
            SnackbarItem(
                Icons.Default.Close,
                errorText
            )
        }
        when (state) {
            is HomeState.DisplayingTasks -> LazyColumn(
                Modifier.padding(5.dp)
            ) {
                items(
                    items = state.tasks,
                    key = {
                        it.id
                    }
                ) { item ->
                    TaskItem(
                        task = item,
                        modifier = Modifier
                            .heightIn(200.dp, 220.dp)
                            .padding(5.dp),
                        onEdit = { onEdit(item.id) },
                        onRemove = { onRemove(item.id) },
                        onInfo = { onInfo(item.id) },
                        onComplete = { onDone(item.id) }
                    )
                }
            }

            is HomeState.Error -> Text(
                state.e.message ?: stringResource(id = R.string.error_message)
            )

            is HomeState.DisplayingQuote -> {
                QuoteDialog(closeQuoteWindow);
            }

            is HomeState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    /*
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize(0.5f)
                            .align(Alignment.Center)
                    )

                     */
                }
            }

            else -> throw AssertionError()
        }
    }
}


@Preview
@Composable
fun TasksWindowPreview() {
    TasksWindow(navigator = EmptyDestinationsNavigator)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarItem(
    imageVector: ImageVector,
    text: String,
) {
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
                Button(
                    onClick = {
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