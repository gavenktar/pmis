package by.bsuir.kirylarol.wolfquotes.Screens.EditQuoteScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.bsuir.kirylarol.destinations.QuotesWindowDestination
import by.bsuir.kirylarol.wolfquotes.MVI.subscribe
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import by.bsuir.kirylarol.wolftasks.Screens.InfoWindow.container
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.koin.core.parameter.parametersOf

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun EditQuoteWindow(
    navigator: DestinationsNavigator = EmptyDestinationsNavigator,
    initial: EditQuoteWindowState?,
) {
    val container = container<EditQuoteViewModel, _, _, _> { parametersOf(initial) }
    container.subscribe(onEvent = { event ->
        when (event) {
            is EditQuoteWindowAction.GoBack -> {
                navigator.navigate(QuotesWindowDestination);
            }

            else -> {}
        }
    }
    )
    EditQuoteWindowContent(state = container.states.collectAsStateWithLifecycle().value,
        intent = { intent ->
            container.intent(intent)
        })
}


@Composable
fun EditQuoteWindowContent(
    state: EditQuoteWindowState,
    intent: (EditQuoteWindowIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = { newValue ->
                run {
                    intent(EditQuoteWindowIntent.ChangeQuote(newValue))
                }
            },
            label = { Text("Цитата") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = state.author,
            onValueChange = { newValue ->
                run {
                    intent(EditQuoteWindowIntent.ChangeAuthor(newValue))
                }
            },
            label = { Text("Автор") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { intent(EditQuoteWindowIntent.ClickSave) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Сохранить")
        }
        Spacer(modifier = Modifier.height(3.dp))
        Button(
            onClick = { intent(EditQuoteWindowIntent.GoBack) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Выйти")
        }
        if (state.showConfirmWindow) {
            AlertDialog(
                onDismissRequest = { intent(EditQuoteWindowIntent.CloseExitWindow) },
                title = {
                    Text(text = "Окошко подтверждения?")
                },
                text = {
                    Text(text = "У вас есть несохраненные изменения, вы точно хотите выйти?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            intent(EditQuoteWindowIntent.ConfirmExit)
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            intent(EditQuoteWindowIntent.CloseExitWindow)
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )

        }
    }

}