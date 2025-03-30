package com.mustafa.weathernow.data.weather.sources.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: ApiService = retrofit
        .create(ApiService::class.java)
}