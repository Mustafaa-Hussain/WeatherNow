package com.mustafa.weathernow.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustafa.weathernow.data.pojos.OneResponse

@Dao
interface LocationDao {
    @Query("select * from weather_data limit 1")
    suspend fun getAllWeatherData(): OneResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weaData: OneResponse): Long
}