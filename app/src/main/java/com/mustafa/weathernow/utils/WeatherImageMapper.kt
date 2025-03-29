package com.mustafa.weathernow.utils

import com.mustafa.weathernow.R

fun getWeatherIconRes(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.ic_clear_day
        "01n" -> R.drawable.ic_clear_night
        "02d" -> R.drawable.ic_partly_cloudy_day
        "02n" -> R.drawable.ic_partly_cloudy_night
        "03d", "03n" -> R.drawable.ic_cloudy
        "10d", "10n" -> R.drawable.ic_rain
        "13d" -> R.drawable.ic_snow_day
        "13n" -> R.drawable.ic_snow_night
        else -> R.drawable.ic_weather_placeholder
    }
}