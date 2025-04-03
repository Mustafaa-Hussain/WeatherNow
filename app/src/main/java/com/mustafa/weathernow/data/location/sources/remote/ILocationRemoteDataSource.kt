package com.mustafa.weathernow.data.location.sources.remote

import com.mustafa.weathernow.data.location.pojo.LocationItem

interface ILocationRemoteDataSource {
    suspend fun searchLocation(query: String): List<LocationItem>
}