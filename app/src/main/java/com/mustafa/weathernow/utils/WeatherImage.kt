package com.mustafa.weathernow.utils

class WeatherImage {
    companion object {
        private const val TEMP_IMAGE_BASE_RUL = "https://openweathermap.org/img/w/"
        private const val TEMP_IMAGE_EXTENSION = ".png"

        fun getWeatherImage(iconCode: String) =
            TEMP_IMAGE_BASE_RUL + iconCode + TEMP_IMAGE_EXTENSION

    }
}