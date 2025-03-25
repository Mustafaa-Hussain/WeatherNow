package com.mustafa.weathernow.data

import com.mustafa.weathernow.data.pojos.OneResponse
import com.mustafa.weathernow.data.remote.WeatherRemoteDatasource
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val remoteDatasource: WeatherRemoteDatasource
) {
    suspend fun getAllWeatherData(
        longitude: Double,
        latitude: Double,
        apikey: String,
        exclude: String = "minutely",
        units: String = "standard",
        lang: String = "en"
    ): Flow<OneResponse> {
        return remoteDatasource.getAllWeatherData(
            longitude,
            latitude,
            apikey,
            exclude,
            units,
            lang
        )
    }
}