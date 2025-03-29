package com.mustafa.weathernow.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

class GeoCoderHelper(private val context: Context) {
    fun getCityName(lat: Double?, lon: Double?): String {
        var city = ""
        try {
            city = Geocoder(context, Locale.getDefault()).getFromLocation(
                lat ?: 0.0,
                lon ?: 0.0,
                1
            ).let { if (!it.isNullOrEmpty()) it.first().subAdminArea else "" }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return city
    }
}