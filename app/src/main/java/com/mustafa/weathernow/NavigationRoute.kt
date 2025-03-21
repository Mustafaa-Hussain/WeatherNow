package com.mustafa.weathernow

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {
    @Serializable
    object HomeScreen : NavigationRoute()
}