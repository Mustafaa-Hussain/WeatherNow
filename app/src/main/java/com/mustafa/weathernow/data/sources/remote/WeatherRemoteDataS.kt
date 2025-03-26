package com.mustafa.weathernow.data.sources.remote

import com.mustafa.weathernow.data.pojos.OneResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDatasource {

    suspend fun getAllWeatherData(
        longitude: Double,
        latitude: Double,
        apikey: String,
        exclude: String,
        units: String,
        lang: String
    ): OneResponse

}