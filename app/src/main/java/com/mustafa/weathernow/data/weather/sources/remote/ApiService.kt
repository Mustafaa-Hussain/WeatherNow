package com.mustafa.weathernow.data.weather.sources.remote

import com.mustafa.weathernow.data.weather.pojos.OneResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3.0/onecall")
    suspend fun getAllWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): OneResponse
}