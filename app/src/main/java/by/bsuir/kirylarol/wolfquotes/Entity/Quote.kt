package by.bsuir.kirylarol.wolfquotes.Entity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Quote(
    val title: String = "",
    val description: String = "",
    val dateCreated: LocalDate = LocalDate.now(),
    val dateEnd: LocalDate = LocalDate.now(),
    var completed: Boolean = false,
    val id: UUID  = UUID.randomUUID()
    )



@Composable
fun CardIcon(
    iconID:  ImageVector,
    onClick:  (Quote) -> Unit,
    modifier: Modifier = Modifier,
    content : String,
    quote : Quote
){

    IconButton(
        modifier = modifier
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.shapes.small
            )
            .aspectRatio(1.0f),
        onClick = { onClick(quote) },
    ) {
        Icon(
            imageVector = iconID,
            contentDescription = content,
            modifier = modifier.fillMaxSize(),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun QuoteItem(
    quote : Quote,
    onInfo : (Quote) -> Unit,
    onComplete : (UUID) -> Unit,
    onEdit : (Quote) -> Unit,
    onRemove : (Quote) -> Unit,
    modifier: Modifier = Modifier
)
{
    Card (
        modifier = modifier.fillMaxSize(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.secondary)
        ) {

        Row (modifier = Modifier.fillMaxSize()) {
            Column (modifier = modifier.fillMaxWidth(0.5f)){
                Text(
                    text = quote.title,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier.fillMaxSize()
                ){
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(10.dp)
                    ){
                        DateCard(date = quote.dateCreated, modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .padding(5.dp))
                        DateCard(date = quote.dateEnd, modifier = Modifier
                            .fillMaxHeight()
                            .padding(5.dp))
                    }
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ){
                Box( modifier  = Modifier
                    .fillMaxHeight()
                    .align(Alignment.End)
                    .weight(1f)
                ) {
                    var Icon = Icons.Default.Close;
                    if (!quote.completed){
                        Icon = Icons.Default.Check
                    }
                    CardIcon(
                        modifier = modifier,
                        iconID = Icon,
                        onClick ={ onComplete(quote.id) },
                        content = "Complete",
                        quote = quote
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box( modifier  = Modifier
                    .fillMaxHeight()
                    .align(Alignment.End)
                    .weight(1f)
                ) {
                    CardIcon(
                        modifier = modifier,
                        iconID = Icons.Default.Info,
                        onClick = {onInfo(quote)},
                        content = "Info",
                        quote = quote
                    )
                    }
                Spacer(modifier = Modifier.height(5.dp))
                Box( modifier  = Modifier
                    .fillMaxHeight()
                    .align(Alignment.End)
                    .weight(1f)
                ) {
                    CardIcon(
                        modifier = modifier,
                        iconID = Icons.Default.Edit,
                        onClick =  {onEdit(quote)},
                        content = "Edit",
                        quote = quote
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box( modifier  = Modifier
                    .fillMaxHeight()
                    .align(Alignment.End)
                    .weight(1f)
                ) {
                    CardIcon(
                        modifier = modifier,
                        iconID = Icons.Default.Delete,
                        onClick = { onRemove(quote) },
                        content = "Remove",
                        quote = quote
                    )
                    }

                }
            }
        }

    }

@Composable
fun DateCard(
    date : LocalDate,
    modifier: Modifier
){
    val text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    Row (
        modifier = modifier
            .fillMaxSize()
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
        ){
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Date",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(2.dp)
                .fillMaxWidth(0.3f)
                .fillMaxHeight()
                .padding(5.dp)
            ,
            tint = MaterialTheme.colorScheme.primary
            )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
            )
    }
}

@Composable
@Preview(device = "spec:parent=pixel_5,orientation=portrait")
fun QuoteItemPreview(
)
{
    Box(Modifier.fillMaxSize()) {
        QuoteItem(Quote("Сделать сто подтягиваний", "Все понятно?", LocalDate.of(2023,6,6),LocalDate.of(2023,7,6),true), {} , {}, {}, {});
    }
}