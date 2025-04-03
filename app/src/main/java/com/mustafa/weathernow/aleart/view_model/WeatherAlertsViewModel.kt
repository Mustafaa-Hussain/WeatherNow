package com.mustafa.weathernow.aleart.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.repo.ILocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherAlertsViewModel(
    private val locationRepository: ILocationRepository
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<AlertLocation>>(emptyList())
    val alerts = _alerts.asStateFlow()

    //first latitude
    //second longitude
    private val _tempLocation = MutableStateFlow<Pair<Double, Double>>(Pair(0.0, 0.0))
    val tempLocation = _tempLocation.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.getAlerts()
                .collect {
                    _alerts.value = it
                }
        }
    }

    fun deleteAlert(alertLocation: AlertLocation) {
        viewModelScope.launch {
            locationRepository.deleteAlert(alertLocation)
        }
    }

    fun saveAlert(alertLocation: AlertLocation) {
        viewModelScope.launch {
            locationRepository.insertAlert(alertLocation)
        }
    }

    fun undoAlert(alertLocation: AlertLocation?) {
        if (alertLocation != null) {
            viewModelScope.launch {
                locationRepository.insertAlert(alertLocation)
            }
        }
    }

    //first latitude
    //second longitude
    fun getTempLocation() {
        _tempLocation.value = locationRepository.getTempLocation()
    }

    fun resetTempLocation() {
        locationRepository.saveTempLocation(0.0, 0.0)
    }


    class WeatherAlertsViewModelFactory(private val locationRepository: ILocationRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherAlertsViewModel(locationRepository) as T
        }
    }
}