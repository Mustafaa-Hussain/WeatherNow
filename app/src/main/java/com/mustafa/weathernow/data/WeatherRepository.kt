package com.mustafa.weathernow.data

import android.util.Log
import com.mustafa.weathernow.data.pojos.OneResponse
import com.mustafa.weathernow.data.sources.remote.WeatherRemoteDatasource
import kotlinx.coroutines.flow.Flow

class WeatherRepository private constructor(
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


    companion object {
        @Volatile
        private var instance: WeatherRepository? = null


        fun getInstance(remoteDatasource: WeatherRemoteDatasource): WeatherRepository {
            return instance ?: synchronized(this) {
                val tempInstance = WeatherRepository(remoteDatasource)
                instance = tempInstance
                tempInstance
            }
        }
    }
}