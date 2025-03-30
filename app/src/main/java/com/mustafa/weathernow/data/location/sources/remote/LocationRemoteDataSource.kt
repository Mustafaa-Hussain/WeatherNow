package com.mustafa.weathernow.data.location.sources.remote

import com.mustafa.weathernow.data.location.pojo.LocationItem

class LocationRemoteDataSource(
    private val searchApiService: SearchApiService
) : ILocationRemoteDataSource {
    override suspend fun searchLocation(query: String): List<LocationItem> {
        return searchApiService.searchLocation(query)
    }
}