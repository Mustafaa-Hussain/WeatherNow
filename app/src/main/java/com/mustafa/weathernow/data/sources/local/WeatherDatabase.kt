package com.mustafa.weathernow.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mustafa.weathernow.data.pojos.OneResponse
import com.mustafa.weathernow.data.pojos.OneResponseConverterFactory

@Database(entities = [OneResponse::class], version = 1, exportSchema = false)
@TypeConverters(OneResponseConverterFactory::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getLocationDao(): LocationDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                instance = tempInstance
                tempInstance
            }
        }
    }
}