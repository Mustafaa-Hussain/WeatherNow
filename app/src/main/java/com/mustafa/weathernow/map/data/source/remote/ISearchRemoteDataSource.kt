package com.mustafa.weathernow.map.data.source.remote

import com.mustafa.weathernow.map.data.pojo.SearchItem

interface ISearchRemoteDataSource {
    suspend fun searchLocation(query: String): List<SearchItem>
}