package by.bsuir.kirylarol.wolfquotes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Job
import java.time.LocalDate
import java.util.UUID
import kotlin.reflect.KFunction7


@OptIn(ExperimentalMaterial3Api::class)
@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun EditQuote(
    quoteUUID: UUID?,
    navigator: DestinationsNavigator = EmptyDestinationsNavigator,
    changeMode : Boolean
) {
    val viewModel = viewModel<EditQuoteViewModel> { EditQuoteViewModel(quoteUUID) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        if ((state as? EditViewState.EditingQuote)?.saved == true) navigator.navigateUp()
    }

    fun onBack (){
        navigator.navigateUp()
    }



    EditQuoteContent(
        state = state,
        onSave = viewModel::onClickSave,
        onBack = ::onBack,
        changeMode = changeMode
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuoteContent(
    state: EditViewState,
    onSave:  (String, String, LocalDate, LocalDate, Boolean, UUID? )  -> Unit,
    onBack: () -> Unit,
    changeMode : Boolean
) {
        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 400.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is EditViewState.Error -> Text(state.e.message ?: stringResource(id = R.string.error_message))
                is EditViewState.Loading -> CircularProgressIndicator()
                is EditViewState.EditingQuote -> {
                    var showBottomSheet by remember { mutableStateOf(true) }
                    var title by remember(state.title) { mutableStateOf(state.title) }
                    var description by remember(state.description) { mutableStateOf(state.description) }
                    var dateCreated by remember(state.dateCreated) { mutableStateOf(state.dateCreated) }
                    var dateEnd by remember(state.dateEnd) { mutableStateOf(state.dateEnd) }
                    var completed by remember(state.completed) { mutableStateOf(state.completed) }
                    val sheetState = rememberModalBottomSheetState(false)
                    val start = dateCreated.toEpochDay()  * 24 * 60 * 60 * 1000L;
                    val end = dateEnd.toEpochDay()  * 24 * 60 * 60 * 1000L;
                    val startState = rememberDatePickerState(initialSelectedDateMillis = start , initialDisplayMode = DisplayMode.Input)
                    val endState = rememberDatePickerState(initialSelectedDateMillis = end ,initialDisplayMode = DisplayMode.Input)

                    Icon(
                        painter = painterResource(id = R.drawable.wolfedit),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.secondary
                    )

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                onBack()
                            },
                            sheetState = sheetState,
                            containerColor = MaterialTheme.colorScheme.background,

                            ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(5.dp)
                            ) {
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { newText ->
                                        title = newText
                                    },
                                    label = { Text(stringResource(id = R.string.nameofquoute)) },
                                    modifier = Modifier
                                        .align(CenterHorizontally)
                                        .fillMaxWidth()
                                        .fillMaxSize()
                                        .weight(1f)
                                        .padding(5.dp),
                                    shape = MaterialTheme.shapes.small,
                                    enabled = changeMode
                                )
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { newText ->
                                        description = newText
                                    },
                                    label = { Text(stringResource(id = R.string.descriptionofquote)) },
                                    modifier = Modifier
                                        .align(CenterHorizontally)
                                        .fillMaxWidth()
                                        .fillMaxSize()
                                        .weight(2f)
                                        .padding(5.dp),
                                    shape = MaterialTheme.shapes.small,
                                    enabled = changeMode
                                )

                                if (changeMode) {

                                    DatePicker(
                                        state = startState,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                    DatePicker(
                                        state = endState,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    )
                                }else{
                                    OutlinedTextField(
                                        value = dateCreated.toString(),
                                        onValueChange = {
                                        },
                                        label = { Text(stringResource(id = R.string.startdate)) },
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                            .fillMaxWidth()
                                            .fillMaxSize()
                                            .weight(1f)
                                            .padding(5.dp),
                                        shape = MaterialTheme.shapes.small,
                                        enabled = changeMode
                                    )
                                    OutlinedTextField(
                                        value = dateEnd.toString(),
                                        onValueChange = {
                                        },
                                        label = { Text(stringResource(id = R.string.finishdate)) },
                                        modifier = Modifier
                                            .align(CenterHorizontally)
                                            .fillMaxWidth()
                                            .fillMaxSize()
                                            .weight(1f)
                                            .padding(5.dp),
                                        shape = MaterialTheme.shapes.small,
                                        enabled = changeMode
                                    )

                                }

                                FilledTonalButton(
                                    onClick = {
                                        if (changeMode) {
                                                onSave(
                                                    title,
                                                    description,
                                                    dateCreated,
                                                    dateEnd,
                                                    false,
                                                    state.id,
                                                )
                                                onBack();
                                        }else{
                                            onBack();
                                        }
                                    }, modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .fillMaxSize()
                                        .weight(0.8f),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                    shape = MaterialTheme.shapes.medium
                                )
                                {
                                    Text(
                                        stringResource(id = R.string.submit),
                                        modifier = Modifier,
                                        style = MaterialTheme.typography.titleMedium

                                    )
                                }
                                Spacer(modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                }
            }
        }
}

