package com.mustafa.weathernow.data.weather.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustafa.weathernow.data.weather.pojos.OneResponse

@Dao
interface WeatherDao {
    @Query("select * from weather_data limit 1")
    suspend fun getAllWeatherData(): OneResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weaData: OneResponse): Long
}