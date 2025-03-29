package com.mustafa.weathernow.map.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.data.location.pojo.SearchItem
import com.mustafa.weathernow.data.location.repo.ISearchRepository
import com.mustafa.weathernow.data.settings.repo.ISettingsRepository
import com.mustafa.weathernow.utils.NavigationRoute.MapSources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class MapViewModel(
    private val settingsRepository: ISettingsRepository,
    private val searchRepository: ISearchRepository
) : ViewModel() {
    private val _longitude = MutableStateFlow(0.0)
    val longitude = _longitude.asStateFlow()

    private val _latitude = MutableStateFlow(0.0)
    val latitude = _latitude.asStateFlow()

    private val _searchResults = MutableSharedFlow<List<SearchItem>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val searchResults = _searchResults.asSharedFlow()

    init {
        _longitude.value = settingsRepository.getLongitude().toDouble()
        _latitude.value = settingsRepository.getLatitude().toDouble()
    }


    fun saveLocation(point: GeoPoint?, source: String) {
        if (point == null) return

        when (source) {
            MapSources.HOME_SCREEN -> saveMapLocation(point.longitude, point.latitude)
            MapSources.SETTING_SCREEN -> saveMapLocation(point.longitude, point.latitude)
            MapSources.FAVORITE_SCREEN -> saveFavLocation(point.longitude, point.latitude)
            MapSources.WEATHER_ALERT_SCREEN -> saveAlarmLocation(point.longitude, point.latitude)
        }
    }

    private fun saveMapLocation(longitude: Double, latitude: Double) {
        settingsRepository.saveLatitude(latitude.toFloat())
        settingsRepository.saveLongitude(longitude.toFloat())
    }

    private fun saveFavLocation(longitude: Double, latitude: Double) {}

    private fun saveAlarmLocation(longitude: Double, latitude: Double) {}

    fun searchLocation(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isNotBlank()) {
                try {
                    val result = searchRepository.searchLocation(query)
                    _searchResults.emit(result)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                _searchResults.emit(listOf())
            }
        }
    }


    class MapViewModelFactory(
        private val settingsRepository: ISettingsRepository,
        private val searchRepository: ISearchRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(settingsRepository, searchRepository) as T
        }
    }
}