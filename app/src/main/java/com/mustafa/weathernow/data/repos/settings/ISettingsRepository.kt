package com.mustafa.weathernow.data.repos.settings

interface ISettingsRepository {

    fun getMeasurementSystem(): String
    fun saveMeasurementSystem(measurementSystem: String)

    fun getLanguage(): String
    fun saveLanguage(language: String)

    fun getLocationFinder(): String
    fun saveLocationFinder(locationFinder: String)

    fun getLongitude(): Float
    fun saveLongitude(longitude: Float)

    fun getLatitude(): Float
    fun saveLatitude(latitude: Float)
}