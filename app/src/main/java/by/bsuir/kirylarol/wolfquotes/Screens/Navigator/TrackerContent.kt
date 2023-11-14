package by.bsuir.kirylarol.wolfquotes.Screens.Navigator

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import by.bsuir.kirylarol.wolfquotes.R
import by.bsuir.kirylarol.wolfquotes.Screens.NavGraphs
import by.bsuir.kirylarol.wolfquotes.Screens.appCurrentDestinationAsState
import by.bsuir.kirylarol.wolfquotes.Screens.destinations.AboutScreenDestination
import by.bsuir.kirylarol.wolfquotes.Screens.destinations.DirectionDestination
import by.bsuir.kirylarol.wolfquotes.Screens.destinations.QuotesWindowDestination
import com.google.accompanist.navigation.material.*
import com.ramcosta.*;
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine


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
    val showBottomBar by remember { derivedStateOf { currentDestination.value !in excludeList } }
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
                icon = { NavBarBadge(it, contentColor, badge) },
                label = { NavBarLabel(it) },
                alwaysShowLabel = false,
            )
        }
    }
}

@Composable
fun NavBarLabel(it: NavigationBarItem) {
    Text(text = it.name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBarBadge(it: NavigationBarItem, contentColor: Color, badge: (NavigationBarItem) -> Int?) {
    Icon(imageVector = it.icon , contentDescription = it.name)
}

enum class NavigationBarItem(val icon: ImageVector, @StringRes val label: Int) { // у энамов могут быть параметры в котлине
    Home(Icons.Default.Home, R.string.bottom_bar_label_home),
    About(Icons.Default.Info, R.string.bottom_bar_label_about)
}

enum class DisabledBottomBar(@StringRes val label: Int){
    About(R.string.bottom_bar_label_about),
}

internal val NavController.bottomBarHandler
    get() = { it: NavigationBarItem -> navigateTopLevel(it.destination) }

internal fun NavController.navigateTopLevel(destination: DirectionDestination) = navigate(destination) {
    launchSingleTop = true
}

internal val NavigationBarItem.destination: DirectionDestination
    get() = when (this) {
        NavigationBarItem.Home -> QuotesWindowDestination
        NavigationBarItem.About -> AboutScreenDestination
    }

internal val DisabledBottomBar.destination: DirectionDestination
    get() = when (this) {
        DisabledBottomBar.About -> AboutScreenDestination
    }

internal val BottomBarDestinations = NavigationBarItem.entries.map { it.destination }.toSet()
internal val excludeList = DisabledBottomBar.entries.map{it.destination}.toSet()
