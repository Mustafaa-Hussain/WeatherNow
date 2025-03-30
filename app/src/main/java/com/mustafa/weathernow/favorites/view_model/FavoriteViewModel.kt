package com.mustafa.weathernow.favorites.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.data.location.repo.ILocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: ILocationRepository) : ViewModel() {
    private val _favoriteLocations = MutableStateFlow<List<FavoriteLocation>>(listOf())
    val favoriteLocations = _favoriteLocations.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getFavoriteLocations().collect {
                _favoriteLocations.value = it
            }
        }
    }

    fun deleteFavoriteLocations(location: FavoriteLocation) {
        viewModelScope.launch {
            repo.deleteFavoriteLocation(location)
        }
    }

    fun undoFavoriteLocation(location: FavoriteLocation?) {
        if (location != null) {
            viewModelScope.launch {
                repo.insertFavoriteLocation(location)
            }
        }
    }

    class FavoriteViewModelFactory(private val repo: ILocationRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(repo) as T
        }
    }
}