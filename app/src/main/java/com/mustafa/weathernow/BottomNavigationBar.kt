package com.mustafa.weathernow

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mustafa.weathernow.utils.NavigationRoute
import kotlin.reflect.KClass


private fun getBottomNavigationList(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            route = NavigationRoute.HomeScreen
        ),
        NavigationItem(
            title = "Favorites",
            icon = Icons.Default.Favorite,
            route = NavigationRoute.FavoriteScreen
        ),
        NavigationItem(
            title = "Alerts",
            icon = Icons.Default.Notifications,
            route = NavigationRoute.WeatherAlertScreen
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            route = NavigationRoute.SettingScreen
        ),
    )
}


class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: NavigationRoute
)

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar {
        getBottomNavigationList().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(item.route::class)
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (currentDestination?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            } == true)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onPrimary
                    )

                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
