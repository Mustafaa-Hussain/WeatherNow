package com.mustafa.weathernow.main_screen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafa.weathernow.R
import com.mustafa.weathernow.aleart.view.WeatherAlertsScreen
import com.mustafa.weathernow.favorites.view.FavoritesScreen
import com.mustafa.weathernow.home.view.HomeScreen
import com.mustafa.weathernow.settings.view.SettingsScreen
import com.mustafa.weathernow.utils.NavigationRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Image(
            painter = painterResource(R.drawable.app_background),
            contentDescription = stringResource(R.string.app_background),
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        BottomNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRoute.HomeScreen
    ) {
        composable<NavigationRoute.HomeScreen> {
            HomeScreen()
        }
        composable<NavigationRoute.WeatherAlertScreen> {
            WeatherAlertsScreen()
        }
        composable<NavigationRoute.FavoriteScreen> {
            FavoritesScreen()
        }
        composable<NavigationRoute.SettingScreen> {
            SettingsScreen()
        }
    }
}