package com.mustafa.weathernow.data.pojos

data class OneResponse(
    val current: Current? = null,
    val timezone: String? = null,
    val timezoneOffset: Int? = null,
    val daily: List<DailyItem?>? = null,
    val lon: Double? = null,
    val hourly: List<HourlyItem?>? = null,
    val lat: Double? = null
)

data class FeelsLike(
    val eve: Double? = null,
    val night: Double? = null,
    val day: Double? = null,
    val morn: Double? = null
)

data class DailyItem(
    val moonset: Int? = null,
    val summary: String? = null,
    val sunrise: Int? = null,
    val temp: Temp? = null,
    val moonPhase: Double? = null,
    val uvi: Double? = null,
    val moonrise: Int? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: FeelsLike? = null,
    val windGust: Double? = null,
    val dt: Int? = null,
    val pop: Int? = null,
    val windDeg: Int? = null,
    val dewPoint: Double? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null
)

data class HourlyItem(
    val temp: Double? = null,
    val visibility: Int? = null,
    val uvi: Double? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Double? = null,
    val windGust: Double? = null,
    val dt: Int? = null,
    val pop: Int? = null,
    val windDeg: Int? = null,
    val dewPoint: Double? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null
)

data class WeatherItem(
    val icon: String? = null,
    val description: String? = null,
    val main: String? = null,
    val id: Int? = null
)

data class Current(
    val sunrise: Int? = null,
    val temp: Double? = null,
    val visibility: Int? = null,
    val uvi: Double? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Double? = null,
    val windGust: Double? = null,
    val dt: Int? = null,
    val windDeg: Int? = null,
    val dewPoint: Double? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null
)

data class Temp(
    val min: Double? = null,
    val max: Double? = null,
    val eve: Double? = null,
    val night: Double? = null,
    val day: Double? = null,
    val morn: Double? = null
)

