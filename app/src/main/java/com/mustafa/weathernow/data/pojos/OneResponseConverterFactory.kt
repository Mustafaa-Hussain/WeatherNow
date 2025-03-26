package com.mustafa.weathernow.data.pojos

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OneResponseConverterFactory {

    private val gson = Gson()

    // OneResponse converters
    @TypeConverter
    fun fromOneResponse(oneResponse: OneResponse?): String? {
        return gson.toJson(oneResponse)
    }

    @TypeConverter
    fun toOneResponse(oneResponseString: String?): OneResponse? {
        if (oneResponseString == null) return null
        return gson.fromJson(oneResponseString, OneResponse::class.java)
    }

    // AlertsItem converters
    @TypeConverter
    fun fromAlertsItemList(alerts: List<AlertsItem?>?): String? {
        return gson.toJson(alerts)
    }

    @TypeConverter
    fun toAlertsItemList(alertsString: String?): List<AlertsItem?>? {
        if (alertsString == null) return null
        val type = object : TypeToken<List<AlertsItem?>?>() {}.type
        return gson.fromJson(alertsString, type)
    }

    // Current converters
    @TypeConverter
    fun fromCurrent(current: Current?): String? {
        return gson.toJson(current)
    }

    @TypeConverter
    fun toCurrent(currentString: String?): Current? {
        if (currentString == null) return null
        return gson.fromJson(currentString, Current::class.java)
    }

    // DailyItem converters
    @TypeConverter
    fun fromDailyItemList(daily: List<DailyItem?>?): String? {
        return gson.toJson(daily)
    }

    @TypeConverter
    fun toDailyItemList(dailyString: String?): List<DailyItem?>? {
        if (dailyString == null) return null
        val type = object : TypeToken<List<DailyItem?>?>() {}.type
        return gson.fromJson(dailyString, type)
    }

    // HourlyItem converters
    @TypeConverter
    fun fromHourlyItemList(hourly: List<HourlyItem?>?): String? {
        return gson.toJson(hourly)
    }

    @TypeConverter
    fun toHourlyItemList(hourlyString: String?): List<HourlyItem?>? {
        if (hourlyString == null) return null
        val type = object : TypeToken<List<HourlyItem?>?>() {}.type
        return gson.fromJson(hourlyString, type)
    }

    // MinutelyItem converters
    @TypeConverter
    fun fromMinutelyItemList(minutely: List<MinutelyItem?>?): String? {
        return gson.toJson(minutely)
    }

    @TypeConverter
    fun toMinutelyItemList(minutelyString: String?): List<MinutelyItem?>? {
        if (minutelyString == null) return null
        val type = object : TypeToken<List<MinutelyItem?>?>() {}.type
        return gson.fromJson(minutelyString, type)
    }

    // WeatherItem converters
    @TypeConverter
    fun fromWeatherItemList(weather: List<WeatherItem?>?): String? {
        return gson.toJson(weather)
    }

    @TypeConverter
    fun toWeatherItemList(weatherString: String?): List<WeatherItem?>? {
        if (weatherString == null) return null
        val type = object : TypeToken<List<WeatherItem?>?>() {}.type
        return gson.fromJson(weatherString, type)
    }

    // Temp converters
    @TypeConverter
    fun fromTemp(temp: Temp?): String? {
        return gson.toJson(temp)
    }

    @TypeConverter
    fun toTemp(tempString: String?): Temp? {
        if (tempString == null) return null
        return gson.fromJson(tempString, Temp::class.java)
    }

    // FeelsLike converters
    @TypeConverter
    fun fromFeelsLike(feelsLike: FeelsLike?): String? {
        return gson.toJson(feelsLike)
    }

    @TypeConverter
    fun toFeelsLike(feelsLikeString: String?): FeelsLike? {
        if (feelsLikeString == null) return null
        return gson.fromJson(feelsLikeString, FeelsLike::class.java)
    }
}