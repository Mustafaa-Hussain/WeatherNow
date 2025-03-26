package com.mustafa.weathernow.home.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mustafa.weathernow.BuildConfig
import com.mustafa.weathernow.data.IWeatherRepository
import com.mustafa.weathernow.data.ResponseState
import com.mustafa.weathernow.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: IWeatherRepository) : ViewModel() {

    private val _weatherResponse = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val weatherResponse = _weatherResponse.asStateFlow()


    fun getWeatherData(
        longitude: Double?,
        latitude: Double?,
        apikey: String = BuildConfig.API_KEY,
        exclude: String = "minutely",
        units: String = "standard",
        lang: String = "en"
    ) {
        viewModelScope.launch {
            try {
                repo.getAllWeatherData(
                    longitude,
                    latitude,
                    apikey,
                    exclude,
                    units,
                    lang
                ).catch {
                    Log.d("```TAG```", "getWeatherData flow catch: ${it.message}")
                }.collect {
                    _weatherResponse.value = ResponseState.Success(it)
                    Log.i("```TAG```", "Response: $it")
                }
            } catch (ex: Exception) {
                _weatherResponse.value = ResponseState.Failure(CONNECTION_ERROR)
                Log.d("```TAG```", "getWeatherData try catch: ${ex.message}")
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    class HomeViewModelFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }

    companion object Errors {
        const val CONNECTION_ERROR = "connection_error"
    }
}