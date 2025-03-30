package com.mustafa.weathernow.data.location.sources.remote

import com.mustafa.weathernow.data.location.pojo.SearchItem
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search")
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<SearchItem>
}