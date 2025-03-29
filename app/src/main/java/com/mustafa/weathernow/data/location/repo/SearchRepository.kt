package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.SearchItem
import com.mustafa.weathernow.data.location.sources.remote.ISearchRemoteDataSource

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
