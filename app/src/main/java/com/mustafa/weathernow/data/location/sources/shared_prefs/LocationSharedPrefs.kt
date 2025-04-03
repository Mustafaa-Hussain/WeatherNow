package com.mustafa.weathernow.data.location.sources.shared_prefs

import android.content.Context
import com.mustafa.weathernow.utils.FileNames
import androidx.core.content.edit

class LocationSharedPrefs(private val context: Context) {

    fun saveTempLocation(longitude: Double, latitude: Double) {
        val sharedPrefs =
            context.getSharedPreferences(
                FileNames.TEMP_LOCATION_FILE_NAME,
                Context.MODE_PRIVATE
            )

        sharedPrefs.edit() {
            putFloat("longitude", longitude.toFloat())
            putFloat("latitude", latitude.toFloat())
        }
    }


    fun getTempLocation(): Pair<Double, Double> {
        val sharedPrefs =
            context.getSharedPreferences(
                FileNames.TEMP_LOCATION_FILE_NAME,
                Context.MODE_PRIVATE
            )

        val latitude = sharedPrefs.getFloat("latitude", 0f).toDouble()
        val longitude = sharedPrefs.getFloat("longitude", 0f).toDouble()
        return Pair(latitude, longitude)
    }
}