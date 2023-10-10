package by.bsuir.kirylarol.wolfquotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.bsuir.kirylarol.wolfquotes.destinations.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun EditQuote(
    quote: Quote = Quote(),
    navigator: DestinationsNavigator = EmptyDestinationsNavigator
){
    var showBottomSheet by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf(quote.title) }
    var dateCreated by remember {
        mutableStateOf(quote.dateCreated.toString())
    }
    var description by remember {
        mutableStateOf(quote.description)
    }
    var dateEnd by remember {
        mutableStateOf(quote.dateEnd.toString())
    }
    val sheetState = rememberModalBottomSheetState()

    Icon(painter  = painterResource(id =R.drawable.wolfedit),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = MaterialTheme.colorScheme.secondary)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                navigator.popBackStack()
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
                    shape = MaterialTheme.shapes.small
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
                    shape = MaterialTheme.shapes.small
                )
                OutlinedTextField(
                    value = dateCreated,
                    onValueChange = { newText ->
                        dateCreated = newText
                    },
                    label = { Text(stringResource(id = R.string.startdate)) },
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .fillMaxSize()
                        .weight(1f)
                        .padding(5.dp),
                    shape = MaterialTheme.shapes.small
                )
                OutlinedTextField(
                    value = dateEnd,
                    onValueChange = { newText ->
                        dateEnd = newText
                    },
                    label = { Text(stringResource(id = R.string.finishdate)) },
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .fillMaxSize()
                        .weight(1f)
                        .padding(5.dp),
                    shape = MaterialTheme.shapes.small
                )
                Spacer(modifier = Modifier.padding(10.dp))
                FilledTonalButton(
                    onClick = { /*TODO*/ }, modifier = Modifier
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

