package by.bsuir.kirylarol.wolfquotes

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import by.bsuir.kirylarol.wolfquotes.destinations.AboutScreenDestination
import by.bsuir.kirylarol.wolfquotes.destinations.DirectionDestination
import by.bsuir.kirylarol.wolfquotes.destinations.HomeScreenDestination
import by.bsuir.kirylarol.wolfquotes.ui.theme.WolfquotesTheme
import com.google.accompanist.navigation.material.*
import com.ramcosta.*;
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.navigation.popUpTo as popUpTo


@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Preview(device = "spec:parent=pixel_4a,orientation=portrait")
@Composable
fun WolfTracker() {
    val navHostEngine = rememberNavHostEngine()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController().apply {
        navigatorProvider += bottomSheetNavigator
    }

    val currentDestination = navController.appCurrentDestinationAsState() // <- из compose-destinations
    val showBottomBar by remember { derivedStateOf { currentDestination.value in BottomBarDestinations } } // <- опционально - если в приложении есть боттом бар, настройте его согласно документации
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .sizeIn(minWidth = 1.dp, minHeight = 1.dp),
        ) {
            DestinationsNavHost(
                modifier = Modifier.weight(1f),
                navGraph = NavGraphs.root,
                engine = navHostEngine,
                navController = navController,
                dependenciesContainerBuilder = { },
                )
            AnimatedVisibility(showBottomBar) {
                WolfNavigationBar(
                    onItemClick = navController.bottomBarHandler,
                    isSelected = { currentDestination.value == it.destination },
                )
            }
        }
    }
}

@Composable
fun WolfNavigationBar(
    onItemClick: (NavigationBarItem) -> Unit,
    isSelected: (NavigationBarItem) -> Boolean,
    modifier: Modifier = Modifier,
    badge: (NavigationBarItem) -> Int? = { null },
) {
    NavigationBar(
        modifier = modifier,
    ) {
        NavigationBarItem.entries.forEach {
            NavigationBarItem(
                selected = isSelected(it),
                onClick = { onItemClick(it) },
                icon = { NavBarBadge(it, contentColor, badge) }, // самостоятельно сделайте с помощью BadgedBox / Icon / Text
                label = { NavBarLabel(it) },
                alwaysShowLabel = false, // ваше решение
            )
        }
    }
}

@Composable
fun NavBarLabel(it: NavigationBarItem) {
    Text(text = "aboba")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBarBadge(it: NavigationBarItem, contentColor: Color, badge: (NavigationBarItem) -> Int?) {
    BadgedBox(badge = { Badge { Text("8") } }) {
        Icon(
            Icons.Filled.Home,
            contentDescription = "Home"
        )

    }
}

enum class NavigationBarItem(val icon: ImageVector, @StringRes val label: Int) { // у энамов могут быть параметры в котлине
    Home(Icons.Default.Home, R.string.bottom_bar_label_home), // иконки можно найти в material-icons-extended
    About(Icons.Default.Info, R.string.bottom_bar_label_about)
    // добавить как минимум два, остальное добавим позже
}

internal val NavController.bottomBarHandler
    get() = { it: NavigationBarItem -> navigateTopLevel(it.destination) } // возвращает лямбду для навигации по менюшке

internal fun NavController.navigateTopLevel(destination: DirectionDestination) = navigate(destination) {
    launchSingleTop = true // читаем доки чтобы понять что это, про бекстек
    restoreState = true
    popUpTo(NavGraphs.root) {
        inclusive = false
        saveState = true
    }
}

internal val NavigationBarItem.destination: DirectionDestination
    get() = when (this) {
        NavigationBarItem.Home -> HomeScreenDestination
        NavigationBarItem.About -> AboutScreenDestination
    }

internal val BottomBarDestinations = NavigationBarItem.entries.map { it.destination }.toSet()
