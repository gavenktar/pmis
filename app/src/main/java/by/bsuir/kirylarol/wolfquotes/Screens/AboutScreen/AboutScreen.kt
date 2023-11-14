package by.bsuir.kirylarol.wolfquotes.Screens.AboutScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import by.bsuir.kirylarol.wolfquotes.MainActivity
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolfquotes.ui.theme.gabaritoFamily
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun Motivation(){

    val mainStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = gabaritoFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center

    )

    Box() {
        Column() {
            MainActivity.aboutStrings.forEach { id ->
                Text(
                    text = stringResource(id = id),
                    style = mainStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                )
            }
        }
    }
}


@Composable
fun Wolf(){
    val borderColor = if (isSystemInDarkTheme()){
        Color.Cyan;
    }else{
        Color.Black;
    }

    Image(
        painter = painterResource(id = R.drawable.wolf),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(3.dp, borderColor),
                RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))


    )
}

@Composable
fun LinkCard(text : Int, reference: String, drawItem : Int){

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }


    val aboutStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = gabaritoFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center

    )

    Text(
        text = stringResource(id = text),
        style = aboutStyle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp)
    )
    IconButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, reference.toUri())
            launcher.launch(intent);
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .size(50.dp)
    ) {
        Image(
            painter = painterResource(id = drawItem),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
        )
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Preview(device = "spec:parent=pixel_4a,orientation=portrait")
@Destination
@Composable
fun AboutScreen (
    navigator: DestinationsNavigator
) {

    val aboutStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = gabaritoFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center

    )

    val mainStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = gabaritoFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center

    )

    val donateReference = stringResource(id = R.string.donatereference);
    val contactReference = stringResource(id = R.string.telegramreference);
    Column(
        Modifier.verticalScroll(rememberScrollState()),
    )
    {
        Row(Modifier.padding(10.dp)) {
            IconButton(onClick = { navigator.navigateUp() }){
                Icon(imageVector = Icons.Filled.Close,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.secondary)
            }
            Text(
                text = stringResource(id = R.string.welcome),
                style = mainStyle,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
        BoxWithConstraints (
            Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column {
                    Wolf()
                    Spacer(modifier = Modifier.padding(10.dp))
                    Box() {
                        Motivation()
                    }
                }
            } else {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)) {
                    Box(modifier = Modifier.fillMaxWidth(0.3f)) {
                        Wolf()
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Motivation()
                }
            }
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
            , verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.fillMaxWidth(0.5f)) {
                LinkCard(R.string.connect,contactReference, R.drawable.telegram);
            }
            Column(Modifier.fillMaxWidth()) {
                LinkCard(R.string.donate,donateReference, R.drawable.donate);
            }
        }
    }
}