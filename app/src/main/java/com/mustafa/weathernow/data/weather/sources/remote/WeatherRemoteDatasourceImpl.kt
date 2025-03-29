package com.mustafa.weathernow.data.weather.sources.remote

import com.mustafa.weathernow.data.weather.pojos.OneResponse

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