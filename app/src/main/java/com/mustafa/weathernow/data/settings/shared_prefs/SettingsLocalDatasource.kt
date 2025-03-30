package com.mustafa.weathernow.data.settings.shared_prefs

import android.content.SharedPreferences
import androidx.core.content.edit

class SettingsLocalDatasource(val settingsLocalDatasource: SharedPreferences) {
    private val measurementSystem = "measurement_system"
    private val defaultUnitSystem = "default"

    private val language = "lang"
    private val defaultLanguage = "device_lang"

    private val locationFinder = "location"
    private val defaultLocation = "GPS"

    private val longitude = "longitude"
    private val defaultLongitude: Float = 0.0f

    private val latitude = "latitude"
    private val defaultLatitude: Float = 0.0f

    //measurement settings
    fun getMeasurementSystem(): String {
        return settingsLocalDatasource.getString(measurementSystem, defaultUnitSystem)
            ?: defaultUnitSystem
    }

    fun saveMeasurementSystem(measurementSystemValue: String) {
        settingsLocalDatasource.edit() {
            putString(measurementSystem, measurementSystemValue)
        }
    }


    //language settings
    fun getLanguage(): String {
        return settingsLocalDatasource.getString(language, defaultLanguage) ?: defaultLanguage
    }

    fun saveLanguage(lang: String) {
        settingsLocalDatasource.edit() {
            putString(language, lang)
        }
    }


    //location settings
    fun getLocationFinder(): String {
        return settingsLocalDatasource.getString(locationFinder, defaultLocation) ?: defaultLocation
    }

    fun saveLocationFinder(locationFinder: String) {
        settingsLocalDatasource.edit() {
            putString(this@SettingsLocalDatasource.locationFinder, locationFinder)
        }
    }

    //saved long
    fun getLongitude(): Float {
        return settingsLocalDatasource.getFloat(longitude, defaultLongitude)
    }

    fun saveLongitude(long: Float) {
        settingsLocalDatasource.edit() {
            putFloat(longitude, long)
        }
    }

    //saved lat
    fun getLatitude(): Float {
        return settingsLocalDatasource.getFloat(latitude, defaultLatitude)
    }

    fun saveLatitude(lat: Float) {
        settingsLocalDatasource.edit() {
            putFloat(latitude, lat)
        }
    }

}