package com.mustafa.weathernow.data.sources.local

import com.mustafa.weathernow.data.pojos.OneResponse

interface WeatherLocalDatasource {
    suspend fun getAllWeatherData(): OneResponse?
    suspend fun insertWeatherData(weatherData: OneResponse): Long
}