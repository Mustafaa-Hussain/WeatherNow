package com.mustafa.weathernow.map.data.repos

import com.mustafa.weathernow.map.data.pojo.SearchItem
import com.mustafa.weathernow.map.data.source.remote.ISearchRemoteDataSource

class SearchRepository(
    private val remoteDataSource: ISearchRemoteDataSource
) : ISearchRepository {
    override suspend fun searchLocation(query: String): List<SearchItem> {
        return remoteDataSource.searchLocation(query)
    }

    companion object {
        @Volatile
        private var instance: SearchRepository? = null

        fun getInstance(remoteDataSource: ISearchRemoteDataSource): SearchRepository {
            return instance ?: synchronized(this) {
                SearchRepository(remoteDataSource).also { instance = it }
            }
        }
    }
}
