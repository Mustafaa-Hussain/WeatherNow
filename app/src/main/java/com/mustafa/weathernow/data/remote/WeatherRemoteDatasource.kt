package com.mustafa.weathernow.data.remote

import com.mustafa.weathernow.data.pojos.OneResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDatasource {
    private val apiService = RetrofitHelper.retrofitService

    suspend fun getAllWeatherData(
        longitude: Double,
        latitude: Double,
        apikey: String,
        exclude: String,
        units: String,
        lang: String
    ): Flow<OneResponse> {
        return flowOf(
            apiService.getAllWeatherData(
                latitude = latitude,
                longitude = longitude,
                apiKey = apikey,
                exclude = exclude,
                units = units,
                lang = lang
            )
        )
    }

}