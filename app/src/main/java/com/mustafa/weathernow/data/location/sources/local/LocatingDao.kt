package com.mustafa.weathernow.data.location.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocatingDao {
    //favorite locations
    @Query("select * from favorite_locations")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long

    @Delete
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int

    //alerts
    @Query("select * from alerts")
    fun getAllAlerts(): Flow<List<AlertLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alertLocation: AlertLocation): Long

    @Delete
    suspend fun deleteAlert(alertLocation: AlertLocation): Int

    @Query("delete from alerts where id = :alertLocationId")
    suspend fun deleteAlert(alertLocationId: Int): Int
}