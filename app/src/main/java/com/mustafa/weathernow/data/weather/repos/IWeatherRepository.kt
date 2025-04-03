package com.mustafa.weathernow.data.weather.repos

import com.mustafa.weathernow.BuildConfig
import com.mustafa.weathernow.data.weather.pojos.OneResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getAllWeatherData(
        longitude: Double?,
        latitude: Double?,
        apikey: String = BuildConfig.API_KEY,
        exclude: String = "minutely",
        units: String = "standard",
        lang: String = "en"
    ): Flow<OneResponse>


    suspend fun insertWeatherData(weatherData: OneResponse): Long
}