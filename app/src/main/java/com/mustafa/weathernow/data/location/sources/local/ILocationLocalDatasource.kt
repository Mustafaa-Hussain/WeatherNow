package com.mustafa.weathernow.data.location.sources.local

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface ILocationLocalDatasource {
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int

    fun getAllAlerts(): Flow<List<AlertLocation>>
    suspend fun insertAlert(alertLocation: AlertLocation): Long
    suspend fun deleteAlert(alertLocation: AlertLocation): Int
}