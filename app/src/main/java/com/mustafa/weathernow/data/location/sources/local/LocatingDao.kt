package com.mustafa.weathernow.data.location.sources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocatingDao {
    @Query("select * from favorite_locations")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long

    @Delete
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int
}