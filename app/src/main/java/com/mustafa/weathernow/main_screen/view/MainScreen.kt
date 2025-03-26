package com.mustafa.weathernow.main_screen.view

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mustafa.weathernow.R
import com.mustafa.weathernow.aleart.view.WeatherAlertsScreen
import com.mustafa.weathernow.data.WeatherRepository
import com.mustafa.weathernow.data.sources.remote.WeatherRemoteDatasource
import com.mustafa.weathernow.favorites.view.FavoritesScreen
import com.mustafa.weathernow.home.view.HomeScreen
import com.mustafa.weathernow.home.view_model.HomeViewModel
import com.mustafa.weathernow.settings.view.SettingsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(location: Location?) {
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
            location,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
fun BottomNavGraph(
    location: Location?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRoute.HomeScreen
    ) {
        composable<NavigationRoute.HomeScreen> {
            val factory = HomeViewModel.HomeViewModelFactory(
                WeatherRepository(WeatherRemoteDatasource())
            )
            HomeScreen(
                viewModel = viewModel(factory = factory),
                location,
                context = LocalContext.current
            )
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