package com.mustafa.weathernow.data.repos.settings

import com.mustafa.weathernow.data.sources.shared_prefs.SettingsLocalDatasource

class SettingsRepository private constructor(
    private val settingsLocalDatasource: SettingsLocalDatasource
) : ISettingsRepository {

    override fun getMeasurementSystem(): String = settingsLocalDatasource.getMeasurementSystem()

    override fun saveMeasurementSystem(measurementSystem: String) =
        settingsLocalDatasource.saveMeasurementSystem(measurementSystem)

    override fun getLanguage(): String = settingsLocalDatasource.getLanguage()

    override fun saveLanguage(language: String) = settingsLocalDatasource.saveLanguage(language)

    override fun getLocationFinder(): String = settingsLocalDatasource.getLocationFinder()

    override fun saveLocationFinder(locationFinder: String) =
        settingsLocalDatasource.saveLocationFinder(locationFinder)

    override fun getLongitude(): Float = settingsLocalDatasource.getLongitude()
    override fun saveLongitude(longitude: Float) = settingsLocalDatasource.saveLongitude(longitude)

    override fun getLatitude(): Float = settingsLocalDatasource.getLatitude()
    override fun saveLatitude(latitude: Float) = settingsLocalDatasource.saveLatitude(latitude)

    companion object {
        @Volatile
        var instance: ISettingsRepository? = null

        fun getInstance(settingsLocalDatasource: SettingsLocalDatasource): ISettingsRepository {
            return instance ?: synchronized(this) {
                val tempInstance = SettingsRepository(settingsLocalDatasource)
                instance = tempInstance
                tempInstance
            }
        }
    }

}