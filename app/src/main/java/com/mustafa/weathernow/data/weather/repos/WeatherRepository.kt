package com.mustafa.weathernow.data.weather.repos

import com.mustafa.weathernow.data.weather.pojos.OneResponse
import com.mustafa.weathernow.data.weather.sources.local.WeatherLocalDatasource
import com.mustafa.weathernow.data.weather.sources.remote.WeatherRemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository private constructor(
    private val localDataSource: WeatherLocalDatasource,
    private val remoteDatasource: WeatherRemoteDatasource
) : IWeatherRepository {

    override suspend fun getAllWeatherData(
        longitude: Double?,
        latitude: Double?,
        apikey: String,
        exclude: String,
        units: String,
        lang: String
    ): Flow<OneResponse> {

        return flow {
            val localResult = localDataSource.getAllWeatherData()
            if (localResult != null)
                emit(localResult)

            if (longitude != null && latitude != null) {
                val remoteResult =
                    remoteDatasource.getAllWeatherData(
                        longitude,
                        latitude,
                        apikey,
                        exclude,
                        units,
                        lang
                    )
                insertWeatherData(remoteResult)

                emit(remoteResult)
            }
        }

    }


    override suspend fun insertWeatherData(
        weatherData: OneResponse
    ): Long {
        return localDataSource.insertWeatherData(weatherData)
    }

    companion object {
        @Volatile
        private var instance: WeatherRepository? = null


        fun getInstance(
            localDataSource: WeatherLocalDatasource,
            remoteDatasource: WeatherRemoteDatasource
        ): WeatherRepository {
            return instance ?: synchronized(this) {
                val tempInstance = WeatherRepository(
                    localDataSource,
                    remoteDatasource
                )
                instance = tempInstance
                tempInstance
            }
        }
    }
}