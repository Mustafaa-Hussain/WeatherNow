package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.SearchItem

interface ISearchRepository {
    suspend fun searchLocation(query: String): List<SearchItem>
}