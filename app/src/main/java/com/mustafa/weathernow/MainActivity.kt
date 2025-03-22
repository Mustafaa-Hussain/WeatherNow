package com.mustafa.weathernow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.mustafa.weathernow.aleart.view.WeatherAlertsScreen
import com.mustafa.weathernow.favorites.view.FavoritesScreen
import com.mustafa.weathernow.home.view.HomeScreen
import com.mustafa.weathernow.settings.view.SettingsScreen
import com.mustafa.weathernow.ui.theme.WeatherNowTheme
import com.mustafa.weathernow.utils.NavigationRoute
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val splashScreenState = remember { mutableStateOf(true) }
            installSplashScreen().setKeepOnScreenCondition {
                splashScreenState.value
            }

            WeatherNowTheme {
                MainScreen()
                LaunchedEffect(true) {
                    delay(1500)
                    splashScreenState.value = false
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
            },
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
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
}