package com.mustafa.weathernow.data.location.sources.local

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.Flow

class LocationLocalDatasource(
    private val locationDao: LocatingDao
) : ILocationLocalDatasource {
    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return locationDao.getAllFavoriteLocations()
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        return locationDao.insertFavoriteLocation(location)
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        return locationDao.deleteFavoriteLocation(location)
    }

    override fun getAllAlerts(): Flow<List<AlertLocation>> {
        return locationDao.getAllAlerts()
    }

    override suspend fun insertAlert(alertLocation: AlertLocation): Long {
        return locationDao.insertAlert(alertLocation)
    }

    override suspend fun deleteAlert(alertLocation: AlertLocation): Int {
        return locationDao.deleteAlert(alertLocation)
    }
}