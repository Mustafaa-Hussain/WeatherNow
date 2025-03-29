package com.mustafa.weathernow.data.weather.sources.local

import com.mustafa.weathernow.data.weather.pojos.OneResponse


interface WeatherLocalDatasource {
    suspend fun getAllWeatherData(): OneResponse?
    suspend fun insertWeatherData(weatherData: OneResponse): Long
}