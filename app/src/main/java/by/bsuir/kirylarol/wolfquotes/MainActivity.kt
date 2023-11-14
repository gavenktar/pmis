package by.bsuir.kirylarol.wolfquotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import by.bsuir.kirylarol.wolfquotes.Screens.Navigator.WolfTracker
import by.bsuir.kirylarol.wolfquotes.ui.theme.WolfquotesTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi


class MainActivity : ComponentActivity() {
    companion object About{
        val aboutStrings = listOf(
            R.string.about,
            R.string.mission,
            R.string.withlove
            )
    }



    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WolfquotesTheme {
                WolfTracker()
            }
        }
    }
}




