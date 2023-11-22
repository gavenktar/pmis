package by.bsuir.kirylarol.wolfquotes.Screens.AddQuoteDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.bsuir.kirylarol.destinations.QuoteCardDestination
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteSource
import by.bsuir.kirylarol.wolfquotes.Screens.QuoteCards.QuoteViewState
import by.bsuir.kirylarol.wolftasks.Screens.EditWindow.EditViewState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.Job
import org.koin.androidx.compose.koinViewModel
import java.util.UUID
import kotlin.reflect.KFunction3


@OptIn(ExperimentalMaterial3Api::class)
@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun QuoteDialog(
    navigator: DestinationsNavigator = EmptyDestinationsNavigator,
    viewModel: ShowQuoteViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    fun onCardClick (){
        when (state){
            is QuoteDialogState.DisplayingQuote ->{
                val title = (state as QuoteDialogState.DisplayingQuote).title
                val author = (state as QuoteDialogState.DisplayingQuote).author
                val initial = QuoteViewState(
                    title = title,
                    author = author,
                    quoteSource = QuoteSource.QuoteSourceInternet,
                    isInFavorite = false,
                    showSnackBar = false,
                    snackBarMessage = null
                )
                    navigator.navigate(
                        QuoteCardDestination(
                            initial
                        )
                    )
            }

            else -> {}
        }

    }

    QuoteContent(
        state = state,
        onSave = viewModel::onClickAdd,
        closeQuoteWindow = { navigator.navigateUp() },
        onCardClick = ::onCardClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteContent(
    state: QuoteDialogState,
    onSave: (String, String, UUID?) -> Unit,
    closeQuoteWindow: () -> Unit,
    onCardClick : () -> Unit
) {
    val icon = Icons.Default.Info
    when (state) {
        is QuoteDialogState.Error -> {
            Text(
                state.e.message ?: stringResource(id = R.string.error_message)
            )
            TextButton(
                onClick = {
                    closeQuoteWindow();
                }
            ) {
                Text("Выйти")
            }
        }

        is QuoteDialogState.Loading -> {
            println("Загрузка...");
        }

        is QuoteDialogState.DisplayingQuote -> {
            println("Err")
           Dialog(onDismissRequest = { closeQuoteWindow() }) {
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .clickable {
                           onCardClick();
                       }
                       .padding(16.dp),
               ) {
                   Column(
                       modifier = Modifier
                           .padding(16.dp)
                   ) {
                       Spacer(modifier = Modifier.height(16.dp))

                       Text(
                           text = state.title,
                           style = TextStyle(
                               fontWeight = FontWeight.Bold,
                               fontSize = 20.sp
                           )
                       )

                       Spacer(modifier = Modifier.height(8.dp))

                       Text(text = state.author)

                       Spacer(modifier = Modifier.height(16.dp))

                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(horizontal = 8.dp),
                           horizontalArrangement = Arrangement.SpaceBetween
                       ) {
                           TextButton(
                               onClick = {
                                   onSave(state.title, state.author, UUID.randomUUID())
                                   closeQuoteWindow()
                               }
                           ) {
                               Text("Сохранить цитатку")
                           }

                           TextButton(
                               onClick = {
                                   closeQuoteWindow()
                               }
                           ) {
                               Text("Выйти")
                           }
                       }
                   }
           }

            }
        }
    }
}
