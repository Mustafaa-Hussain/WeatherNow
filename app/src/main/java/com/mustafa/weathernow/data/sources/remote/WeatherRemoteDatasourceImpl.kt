package com.mustafa.weathernow.data.sources.remote

import com.mustafa.weathernow.data.pojos.OneResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDatasourceImpl(val apiService: ApiService) : WeatherRemoteDatasource {

    override suspend fun getAllWeatherData(
        longitude: Double,
        latitude: Double,
        apikey: String,
        exclude: String,
        units: String,
        lang: String
    ): OneResponse {
        return apiService.getAllWeatherData(
            latitude = latitude,
            longitude = longitude,
            apiKey = apikey,
            exclude = exclude,
            units = units,
            lang = lang
        )
    }

}