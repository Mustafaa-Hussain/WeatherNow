package com.mustafa.weathernow.data

import com.mustafa.weathernow.data.pojos.OneResponse

sealed class ResponseState {
    object Loading : ResponseState()
    data class Success(val response: OneResponse) : ResponseState()
    data class Failure(val errorMessage: String) : ResponseState()
}