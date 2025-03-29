package com.mustafa.weathernow.map.data.source.remote

import com.mustafa.weathernow.map.data.pojo.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search")
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<SearchItem>
}