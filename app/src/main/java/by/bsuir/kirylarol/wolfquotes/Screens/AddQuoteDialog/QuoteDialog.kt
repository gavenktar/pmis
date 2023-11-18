package by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolftasks.Screens.EditWindow.EditViewState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.Job
import org.koin.androidx.compose.koinViewModel
import java.util.UUID
import kotlin.reflect.KFunction3


@OptIn(ExperimentalMaterial3Api::class)
// @com.ramcosta.composedestinations.annotation.Destination
@Composable
fun QuoteDialog(
    //  navigator: DestinationsNavigator = EmptyDestinationsNavigator,
    closeQuoteWindow: () -> Unit,
    viewModel: ShowQuoteViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init()
    }


    QuoteContent(
        state = state,
        onSave = viewModel::onClickAdd,
        closeQuoteWindow = closeQuoteWindow
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteContent(
    state: QuoteDialogState,
    onSave: (String, String, UUID?) -> Unit,
    closeQuoteWindow: () -> Unit
) {
    val icon = Icons.Default.Info
    when (state) {
        is QuoteDialogState.Error -> Text(
            state.e.message ?: stringResource(id = R.string.error_message)
        )

        is QuoteDialogState.Loading -> {
            println("Загрузка...");
        }

        is QuoteDialogState.DisplayingQuote -> {
            println("Err")
            AlertDialog(
                icon = {
                    Icon(icon, contentDescription = "Example Icon")
                },
                title = {
                    Text(text = state.title)
                },
                text = {
                    Text(text = state.author)
                },
                onDismissRequest = {
                    closeQuoteWindow()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onSave(state.title, state.author, UUID.randomUUID())
                            closeQuoteWindow();
                        }
                    ) {
                        Text("Сохранить цитатку")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            closeQuoteWindow();
                        }
                    ) {
                        Text("Выйти")
                    }
                }
            )
        }
    }
}
