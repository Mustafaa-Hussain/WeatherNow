package com.mustafa.weathernow.map.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.data.location.pojo.LocationItem
import com.mustafa.weathernow.data.location.repo.ILocationRepository
import com.mustafa.weathernow.data.settings.repo.ISettingsRepository
import com.mustafa.weathernow.utils.NavigationRoute.MapSources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

@OptIn(FlowPreview::class)
class MapViewModel(
    private val settingsRepository: ISettingsRepository,
    private val locationRepository: ILocationRepository
) : ViewModel() {
    private val _longitude = MutableStateFlow(0.0)
    val longitude = _longitude.asStateFlow()

    private val _latitude = MutableStateFlow(0.0)
    val latitude = _latitude.asStateFlow()

    private val _searchQuery = MutableSharedFlow<String>(
        1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _searchResults = MutableStateFlow<List<LocationItem>>(listOf())
    val searchResults = _searchResults.asStateFlow()

    init {
        _longitude.value = settingsRepository.getLongitude().toDouble()
        _latitude.value = settingsRepository.getLatitude().toDouble()

        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery
                .debounce(250)
                .distinctUntilChanged()
                .collect { query ->
                    getSearchResults(query)
                }
        }
    }


    fun saveLocation(point: GeoPoint?, source: String): Boolean {
        if (point == null) return false

        when (source) {
            MapSources.HOME_SCREEN, MapSources.SETTING_SCREEN
                -> saveMapLocation(
                point.longitude,
                point.latitude
            )

            MapSources.FAVORITE_SCREEN -> saveFavLocation(point.longitude, point.latitude)
            MapSources.WEATHER_ALERT_SCREEN -> saveAlarmLocation(point.longitude, point.latitude)
        }
        return true
    }

    private fun saveMapLocation(longitude: Double, latitude: Double) {
        settingsRepository.saveLatitude(latitude.toFloat())
        settingsRepository.saveLongitude(longitude.toFloat())
    }

    private fun saveFavLocation(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            locationRepository.insertFavoriteLocation(
                FavoriteLocation(
                    longitude = longitude,
                    latitude = latitude
                )
            )
        }
    }

    private fun saveAlarmLocation(longitude: Double, latitude: Double) {
        //save temp alert location
        locationRepository.saveTempLocation(longitude, latitude)
    }

    fun searchLocation(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    private suspend fun getSearchResults(query: String) {
        if (query.isNotBlank()) {
            try {
                val result = locationRepository.searchLocation(query)
                _searchResults.emit(result)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } else {
            _searchResults.emit(listOf())
        }
    }


    class MapViewModelFactory(
        private val settingsRepository: ISettingsRepository,
        private val searchRepository: ILocationRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(settingsRepository, searchRepository) as T
        }
    }
}