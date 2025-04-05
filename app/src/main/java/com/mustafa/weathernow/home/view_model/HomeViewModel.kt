package com.mustafa.weathernow.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.BuildConfig
import com.mustafa.weathernow.data.settings.repo.ISettingsRepository
import com.mustafa.weathernow.data.weather.repos.IWeatherRepository
import com.mustafa.weathernow.data.weather.repos.WeatherRepository
import com.mustafa.weathernow.utils.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepo: IWeatherRepository,
    private val settingRepository: ISettingsRepository
) : ViewModel() {

    private val _weatherResponse = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val weatherResponse = _weatherResponse.asStateFlow()

    private val _measurementSystem = MutableStateFlow("")
    val measurementSystem = _measurementSystem.asStateFlow()

    private val _language = MutableStateFlow("")
    val language = _language.asStateFlow()

    private val _locationFinder = MutableStateFlow("")
    val locationFinder = _locationFinder.asStateFlow()

    private val _savedLatitude = MutableStateFlow(0f)
    val savedLatitude = _savedLatitude.asStateFlow()

    private val _savedLongitude = MutableStateFlow(0f)
    val savedLongitude = _savedLongitude.asStateFlow()


    fun getWeatherData(
        longitude: Double?,
        latitude: Double?,
        apikey: String = BuildConfig.API_KEY,
        units: String = settingRepository.getMeasurementSystem(),
        lang: String = settingRepository.getLanguage()
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepo.getAllWeatherData(
                    longitude,
                    latitude,
                    apikey,
                    units = units,
                    lang = lang
                ).catch {
                }.collect {
                    _weatherResponse.value = ResponseState.Success(it)
                }
            } catch (ex: Exception) {
                _weatherResponse.value = ResponseState.Failure(CONNECTION_ERROR)
            }
        }
    }


    fun getSavedSettings() {
        getMeasurementSystem()
        getLanguage()
        getLocationFinder()
        getSavedLocation()
    }

    private fun getMeasurementSystem() {
        _measurementSystem.value = settingRepository.getMeasurementSystem()
    }

    private fun getLanguage() {
        _language.value = settingRepository.getLanguage()
    }

    private fun getLocationFinder() {
        _locationFinder.value = settingRepository.getLocationFinder()
    }


    private fun getSavedLocation() {
        _savedLatitude.value = settingRepository.getLatitude()
        _savedLongitude.value = settingRepository.getLongitude()
    }

    @Suppress("UNCHECKED_CAST")
    class HomeViewModelFactory(
        private val weatherRepo: WeatherRepository,
        private val settingRepository: ISettingsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(weatherRepo, settingRepository) as T
        }
    }

    companion object Errors {
        const val CONNECTION_ERROR = "connection_error"
    }
}