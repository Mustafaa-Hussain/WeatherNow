package com.mustafa.weathernow.data.location.sources.remote

import com.mustafa.weathernow.data.location.pojo.SearchItem

interface ISearchRemoteDataSource {
    suspend fun searchLocation(query: String): List<SearchItem>
}