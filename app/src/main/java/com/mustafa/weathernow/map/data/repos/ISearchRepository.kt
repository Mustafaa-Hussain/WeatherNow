package com.mustafa.weathernow.map.data.repos

import com.mustafa.weathernow.map.data.pojo.SearchItem
import kotlinx.coroutines.flow.SharedFlow

interface ISearchRepository {
    suspend fun searchLocation(query: String): List<SearchItem>
}