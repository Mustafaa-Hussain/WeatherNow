package com.mustafa.weathernow.main_screen.view

import android.content.Context
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
import com.mustafa.weathernow.data.repos.settings.SettingsRepository
import com.mustafa.weathernow.data.repos.weather.WeatherRepository
import com.mustafa.weathernow.data.sources.local.WeatherDatabase
import com.mustafa.weathernow.data.sources.local.WeatherLocalDatasourceImpl
import com.mustafa.weathernow.data.sources.remote.RetrofitHelper
import com.mustafa.weathernow.data.sources.remote.WeatherRemoteDatasourceImpl
import com.mustafa.weathernow.data.sources.shared_prefs.SettingsLocalDatasource
import com.mustafa.weathernow.favorites.view.FavoritesScreen
import com.mustafa.weathernow.home.view.HomeScreen
import com.mustafa.weathernow.home.view_model.HomeViewModel
import com.mustafa.weathernow.map.view.MapScreen
import com.mustafa.weathernow.settings.view.SettingsScreen
import com.mustafa.weathernow.settings.view_model.SettingsViewModel
import com.mustafa.weathernow.utils.FileNames
import com.mustafa.weathernow.utils.NavigationRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isLocationPermissionGranted: Boolean,
    location: Location?,
    askForLocationPermission: () -> Unit
) {
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
            isLocationPermissionGranted,
            location,
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            askForLocationPermission
        )
    }
}


@Composable
fun BottomNavGraph(
    isLocationPermissionGranted: Boolean,
    location: Location?,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    askForLocationPermission: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRoute.HomeScreen
    ) {
        composable<NavigationRoute.HomeScreen> {
            val factory = HomeViewModel.HomeViewModelFactory(
                WeatherRepository.getInstance(
                    WeatherLocalDatasourceImpl(
                        WeatherDatabase.getInstance(LocalContext.current).getLocationDao()
                    ),
                    WeatherRemoteDatasourceImpl(RetrofitHelper.retrofitService)
                ),
                SettingsRepository.getInstance(
                    SettingsLocalDatasource(
                        LocalContext.current.getSharedPreferences(
                            FileNames.SETTINGS_FILE_NAME,
                            Context.MODE_PRIVATE
                        )
                    )
                )
            )
            HomeScreen(
                navController,
                viewModel = viewModel(factory = factory),
                isLocationPermissionGranted,
                location
            )
        }
        composable<NavigationRoute.WeatherAlertScreen> {
            WeatherAlertsScreen()
        }
        composable<NavigationRoute.FavoriteScreen> {
            FavoritesScreen()
        }
        composable<NavigationRoute.SettingScreen> {
            SettingsScreen(
                navController,
                viewModel(
                    factory = SettingsViewModel.SettingViewModelFactory(
                        SettingsRepository.getInstance(
                            SettingsLocalDatasource(
                                LocalContext.current.getSharedPreferences(
                                    FileNames.SETTINGS_FILE_NAME,
                                    Context.MODE_PRIVATE
                                )
                            )
                        )
                    )
                ),
                askForLocationPermission
            )
        }
        composable<NavigationRoute.MapLocationFinderScreen> {
            val factory = SettingsViewModel.SettingViewModelFactory(
                SettingsRepository.getInstance(
                    SettingsLocalDatasource(
                        LocalContext.current.getSharedPreferences(
                            FileNames.SETTINGS_FILE_NAME,
                            Context.MODE_PRIVATE
                        )
                    )
                )
            )

            MapScreen(
                navController,
                viewModel(factory = factory)
            )
        }
    }
}