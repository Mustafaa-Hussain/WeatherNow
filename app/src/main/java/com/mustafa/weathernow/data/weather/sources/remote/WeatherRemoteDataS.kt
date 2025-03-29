package com.mustafa.weathernow.data.weather.sources.remote

import com.mustafa.weathernow.data.weather.pojos.OneResponse

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