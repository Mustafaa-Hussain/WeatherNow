package com.mustafa.weathernow.map.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mustafa.weathernow.settings.view_model.SettingsViewModel

@Composable
fun MapScreen(navController: NavController, settingViewModel: SettingsViewModel) {
    Text(text = "Map Screen", fontSize = 22.sp)

    LaunchedEffect(Unit) {
        //this will happen when user select location
        //this now dummy location
        settingViewModel.saveLocation(0.0, 0.0)
        settingViewModel.saveLocationFinder("Map")
    }

}