package com.mustafa.weathernow.data.repos.weather

import com.mustafa.weathernow.data.pojos.OneResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getAllWeatherData(
        longitude: Double?,
        latitude: Double?,
        apikey: String,
        exclude: String = "minutely",
        units: String = "standard",
        lang: String = "en"
    ): Flow<OneResponse>


    suspend fun insertWeatherData(weatherData: OneResponse): Long
}