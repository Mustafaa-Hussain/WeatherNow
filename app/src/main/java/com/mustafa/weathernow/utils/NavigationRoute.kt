package com.mustafa.weathernow.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute() {
    @Serializable
    object HomeScreen : NavigationRoute()

    @Serializable
    object WeatherAlertScreen : NavigationRoute()

    @Serializable
    object FavoriteScreen : NavigationRoute()

    @Serializable
    object SettingScreen : NavigationRoute()

    @Serializable
    data class MapLocationFinderScreen(val sourceScreen: String) : NavigationRoute()

    @Serializable
    data class WeatherPreviewScreen(val longitude: Double, val latitude: Double) : NavigationRoute()

    companion object MapSources {
        const val HOME_SCREEN = "home"
        const val SETTING_SCREEN = "setting"
        const val FAVORITE_SCREEN = "favorite"
        const val WEATHER_ALERT_SCREEN = "weather_alert"
    }
}