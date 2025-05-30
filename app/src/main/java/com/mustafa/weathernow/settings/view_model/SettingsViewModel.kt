package com.mustafa.weathernow.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mustafa.weathernow.data.settings.repo.ISettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repo: ISettingsRepository) : ViewModel() {
    private val _locationFinder = MutableStateFlow("")
    val locationFinder = _locationFinder.asStateFlow()

    private val _language = MutableStateFlow("")
    val language = _language.asStateFlow()

    private val _measurementSystem = MutableStateFlow("")
    val measurementSystem = _measurementSystem.asStateFlow()

    private val _longitude = MutableStateFlow(30.0)
    val longitude = _longitude.asStateFlow()

    private val _latitude = MutableStateFlow(30.0)
    val latitude = _latitude.asStateFlow()


    fun getSettings() {
        _locationFinder.value = getLocationFinder()
        _language.value = getAppLanguage()
        _measurementSystem.value = getMeasurementSystem()
        _longitude.value = getLongitude()
        _latitude.value = getLatitude()
    }


    //location finder
    fun saveLocationFinder(location: String) {
        repo.saveLocationFinder(location)
        _locationFinder.value = location
    }

    private fun getLocationFinder()
            : String {
        return repo.getLocationFinder()
    }

    // save lon & lat
    fun saveLocation(lon: Double, lat: Double) {
        repo.saveLongitude(lon.toFloat())
        repo.saveLatitude(lat.toFloat())
    }

    //get saved longitude and latitude
    private fun getLongitude(): Double {
        return repo.getLongitude().toDouble()
    }

    private fun getLatitude(): Double {
        return repo.getLatitude().toDouble()
    }


    //language
    fun saveAppLanguage(lang: String) {
        repo.saveLanguage(lang)
        _language.value = lang
    }

    private fun getAppLanguage(): String {
        return repo.getLanguage()
    }

    //measurement units
    fun saveMeasurementSystem(measurementSystem: String) {
        repo.saveMeasurementSystem(measurementSystem)
        _measurementSystem.value = measurementSystem
    }

    private fun getMeasurementSystem(): String {
        return repo.getMeasurementSystem()
    }

    @Suppress("UNCHECKED_CAST")
    class SettingViewModelFactory(private val repo: ISettingsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo) as T
        }
    }

}