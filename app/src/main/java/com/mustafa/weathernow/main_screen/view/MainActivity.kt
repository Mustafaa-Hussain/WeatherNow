package com.mustafa.weathernow.main_screen.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafa.weathernow.BuildConfig
import com.mustafa.weathernow.ui.theme.WeatherNowTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            WeatherNowTheme {
                MainScreen()
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen(modifier: Modifier = Modifier) {
        val splashScreenState = remember { mutableStateOf(true) }
        installSplashScreen().setKeepOnScreenCondition {
            splashScreenState.value
        }
        LaunchedEffect(true) {
            delay(1500)
            splashScreenState.value = false
        }
    }

}