package com.mustafa.weathernow.utils

import com.mustafa.weathernow.data.weather.pojos.OneResponse

sealed class ResponseState {
    object Loading : ResponseState()
    data class Success(val response: OneResponse) : ResponseState()
    data class Failure(val errorMessage: String) : ResponseState()
}