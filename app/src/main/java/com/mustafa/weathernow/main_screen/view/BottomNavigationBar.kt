package com.mustafa.weathernow.main_screen.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mustafa.weathernow.R
import com.mustafa.weathernow.main_screen.view.NavigationRoute


private fun getBottomNavigationList(context: Context): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = context.getString(R.string.home),
            icon = Icons.Default.Home,
            route = NavigationRoute.HomeScreen
        ),
        NavigationItem(
            title = context.getString(R.string.favorites),
            icon = Icons.Default.Favorite,
            route = NavigationRoute.FavoriteScreen
        ),
        NavigationItem(
            title = context.getString(R.string.alerts),
            icon = Icons.Default.Notifications,
            route = NavigationRoute.WeatherAlertScreen
        ),
        NavigationItem(
            title = context.getString(R.string.settings),
            icon = Icons.Default.Settings,
            route = NavigationRoute.SettingScreen
        )
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

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.height(80.dp)
    ) {
        getBottomNavigationList(LocalContext.current).forEachIndexed { index, item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                selected = isSelected,
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
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
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
