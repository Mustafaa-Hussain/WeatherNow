package com.mustafa.weathernow.data.location.sources.remote

import com.mustafa.weathernow.data.location.pojo.SearchItem

class SearchRemoteDataSource(
    private val searchApiService: SearchApiService
) : ISearchRemoteDataSource {
    override suspend fun searchLocation(query: String): List<SearchItem> {
        return searchApiService.searchLocation(query)
    }
}