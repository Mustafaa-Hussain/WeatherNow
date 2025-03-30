package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import com.mustafa.weathernow.data.location.pojo.LocationItem
import com.mustafa.weathernow.data.location.sources.local.ILocationLocalDatasource
import com.mustafa.weathernow.data.location.sources.remote.ILocationRemoteDataSource
import kotlinx.coroutines.flow.Flow


class LocationRepository(
    private val remoteDataSource: ILocationRemoteDataSource,
    private val localDataSource: ILocationLocalDatasource
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

    companion object {
        @Volatile
        private var instance: LocationRepository? = null

        fun getInstance(
            remoteDataSource: ILocationRemoteDataSource,
            localDatasource: ILocationLocalDatasource
        ): LocationRepository {
            return instance ?: synchronized(this) {
                LocationRepository(
                    remoteDataSource,
                    localDatasource
                ).also { instance = it }
            }
        }
    }
}
