package by.bsuir.kirylarol.wolfquotes
import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import by.bsuir.kirylarol.wolfquotes.Screens.Navigator.WolfTracker
import by.bsuir.kirylarol.wolfquotes.ui.theme.WolfquotesTheme
import by.bsuir.kirylarol.wolftasks.Screens.QuoteWindow.HomeViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject


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




