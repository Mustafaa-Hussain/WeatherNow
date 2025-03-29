package com.mustafa.weathernow.map.data.source.remote

import com.mustafa.weathernow.map.data.pojo.SearchItem

class SearchRemoteDataSource(
    private val searchApiService: SearchApiService
) : ISearchRemoteDataSource {
    override suspend fun searchLocation(query: String): List<SearchItem> {
        return searchApiService.searchLocation(query)
    }
}