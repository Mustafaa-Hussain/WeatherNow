package com.mustafa.weathernow.data.weather.sources.local

import com.mustafa.weathernow.data.weather.pojos.OneResponse


class WeatherLocalDatasourceImpl(val dao: WeatherDao) : WeatherLocalDatasource {
    override suspend fun getAllWeatherData(): OneResponse? {
        return dao.getAllWeatherData()
    }

    override suspend fun insertWeatherData(weatherData: OneResponse): Long {
        return dao.insertWeatherData(weatherData)
    }
}