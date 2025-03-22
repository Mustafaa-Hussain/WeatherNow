package com.mustafa.weathernow.main_screen.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mustafa.weathernow.ui.theme.WeatherNowTheme
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
}