package com.mustafa.weathernow.data.sources.local

import com.mustafa.weathernow.data.pojos.OneResponse

class WeatherLocalDatasourceImpl(val dao: LocationDao) : WeatherLocalDatasource {
    override suspend fun getAllWeatherData(): OneResponse? {
        return dao.getAllWeatherData()
    }

    override suspend fun insertWeatherData(weatherData: OneResponse): Long {
        return dao.insertWeatherData(weatherData)
    }
}