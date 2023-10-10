package by.bsuir.kirylarol.wolfquotes

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import by.bsuir.kirylarol.wolfquotes.WolfTracker
import by.bsuir.kirylarol.wolfquotes.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
){
    Text(text ="aboba")
}