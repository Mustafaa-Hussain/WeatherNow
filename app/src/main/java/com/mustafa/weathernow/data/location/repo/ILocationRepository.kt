package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.data.location.pojo.LocationItem
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    suspend fun searchLocation(query: String): List<LocationItem>

    fun getFavoriteLocations(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int

    fun saveTempLocation(longitude: Double, latitude: Double)
    fun getTempLocation(): Pair<Double, Double>

    fun getAlerts(): Flow<List<AlertLocation>>
    suspend fun insertAlert(alertLocation: AlertLocation): Long
    suspend fun deleteAlert(alertLocation: AlertLocation): Int
}