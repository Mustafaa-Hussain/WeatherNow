package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.data.location.pojo.LocationItem
import com.mustafa.weathernow.data.location.sources.local.ILocationLocalDatasource
import com.mustafa.weathernow.data.location.sources.remote.ILocationRemoteDataSource
import com.mustafa.weathernow.data.location.sources.shared_prefs.LocationSharedPrefs
import kotlinx.coroutines.flow.Flow


class LocationRepository(
    private val remoteDataSource: ILocationRemoteDataSource,
    private val localDataSource: ILocationLocalDatasource,
    private val sharedPrefsDataSource: LocationSharedPrefs
) : ILocationRepository {
    override suspend fun searchLocation(query: String): List<LocationItem> {
        return remoteDataSource.searchLocation(query)
    }

    override fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return localDataSource.getAllFavoriteLocations()
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        return localDataSource.insertFavoriteLocation(location)
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        return localDataSource.deleteFavoriteLocation(location)
    }

    override fun saveTempLocation(longitude: Double, latitude: Double) {
        sharedPrefsDataSource.saveTempLocation(longitude, latitude)
    }

    override fun getTempLocation(): Pair<Double, Double> {
        return sharedPrefsDataSource.getTempLocation()
    }

    override fun getAlerts(): Flow<List<AlertLocation>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alertLocation: AlertLocation): Long {
        return localDataSource.insertAlert(alertLocation)
    }

    override suspend fun deleteAlert(alertLocation: AlertLocation): Int {
        return localDataSource.deleteAlert(alertLocation)
    }

    override suspend fun deleteAlert(alertLocationId: Int): Int {
        return localDataSource.deleteAlert(alertLocationId)
    }

    companion object {
        @Volatile
        private var instance: LocationRepository? = null

        fun getInstance(
            remoteDataSource: ILocationRemoteDataSource,
            localDatasource: ILocationLocalDatasource,
            sharedPrefsDataSource: LocationSharedPrefs
        ): LocationRepository {
            return instance ?: synchronized(this) {
                LocationRepository(
                    remoteDataSource,
                    localDatasource,
                    sharedPrefsDataSource
                ).also { instance = it }
            }
        }
    }
}
