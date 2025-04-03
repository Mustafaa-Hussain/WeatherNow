package com.mustafa.weathernow.data.location.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation

@Database(
    entities = [FavoriteLocation::class, AlertLocation::class],
    version = 1,
    exportSchema = false
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun getLocatingDao(): LocatingDao

    companion object {
        @Volatile
        private var instance: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            return instance ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).build()
                instance = tempInstance
                tempInstance
            }
        }
    }

}