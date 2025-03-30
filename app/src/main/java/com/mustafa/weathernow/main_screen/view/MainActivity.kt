package com.mustafa.weathernow.main_screen.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mustafa.weathernow.ui.theme.WeatherNowTheme
import com.mustafa.weathernow.utils.LocationFinder
import com.mustafa.weathernow.utils.LocationFinder.Companion.LOCATION_PERMISSION_REQUEST_CODE
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private var location = mutableStateOf<Location?>(null)
    private var locationPermissionGranted by mutableStateOf(true)
    private lateinit var locationFinder: LocationFinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationFinder = LocationFinder(
            this,
            location
        )

        setContent {
            WeatherNowTheme {
                MainScreen(
                    locationPermissionGranted,
                    location.value,
                    locationFinder::gainLocationPermission
                )
                SplashScreen()
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onStart() {
        super.onStart()
        locationFinder.gainLocationPermission()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                locationFinder.getCurrentLocation()
                locationPermissionGranted = true
            } else {
                //send to ui the permission is denied
                locationPermissionGranted = false
            }
        }
    }

    @Composable
    fun SplashScreen() {
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