package com.mustafa.weathernow.data.location.sources.local

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocationLocalDatasource(
    private val favoriteLocations: MutableList<FavoriteLocation> = mutableListOf(),
    private val alerts: MutableList<AlertLocation> = mutableListOf()
) : ILocationLocalDatasource {
    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return flowOf(favoriteLocations)
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        favoriteLocations += location
        return 1
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        favoriteLocations -= location
        return 1
    }

    override fun getAllAlerts(): Flow<List<AlertLocation>> {
        return flowOf(alerts)
    }

    override suspend fun insertAlert(alertLocation: AlertLocation): Long {
        alerts += alertLocation
        return 1
    }

    override suspend fun deleteAlert(alertLocation: AlertLocation): Int {
        alerts -= alertLocation
        return 1
    }

    override suspend fun deleteAlert(alertLocationId: Int): Int {
        return if (alerts.removeIf { it.id.toInt() == alertLocationId }) 1 else 0
    }
}