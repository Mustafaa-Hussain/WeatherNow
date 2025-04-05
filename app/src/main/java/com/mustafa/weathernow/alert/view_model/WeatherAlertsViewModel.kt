package com.mustafa.weathernow.alert.view_model

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

    private val _deletionState = MutableStateFlow(false)
    val deletionStatus = _deletionState.asStateFlow()

    private val _saveState = MutableStateFlow(false)
    val saveStatus = _saveState.asStateFlow()

    private val _undoState = MutableStateFlow(false)
    val undoStatus = _undoState.asStateFlow()

    fun resetState() {
        _deletionState.value = false
        _saveState.value = false
        _undoState.value = false
    }

    //first latitude
    //second longitude
    private val _tempLocation = MutableStateFlow<Pair<Double, Double>>(Pair(0.0, 0.0))
    val tempLocation = _tempLocation.asStateFlow()

    private val _lastAddedAlertId = MutableStateFlow(-1)
    val lastAddedAlertId = _lastAddedAlertId.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.getAlerts()
                .collect {
                    _alerts.value = it
                    _lastAddedAlertId.value = it.lastOrNull()?.id?.toInt() ?: -1
                }
        }
    }

    fun deleteAlert(alertLocation: AlertLocation) {
        viewModelScope.launch {
            val result = locationRepository.deleteAlert(alertLocation)
            _deletionState.value = result > 0
        }
    }

    fun saveAlert(alertLocation: AlertLocation) {
        viewModelScope.launch {
            val result = locationRepository.insertAlert(alertLocation)
            _saveState.emit(result > 0)
        }
    }

    fun undoAlert(alertLocation: AlertLocation?) {
        if (alertLocation != null) {
            viewModelScope.launch {
                val result = locationRepository.insertAlert(alertLocation)
                _undoState.value = result > 0
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