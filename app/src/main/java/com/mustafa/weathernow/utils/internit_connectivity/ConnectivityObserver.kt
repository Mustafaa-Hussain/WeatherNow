package com.mustafa.weathernow.utils.internit_connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    enum class Status {
        Available, UnAvailable, Loosing, Lost
    }
}