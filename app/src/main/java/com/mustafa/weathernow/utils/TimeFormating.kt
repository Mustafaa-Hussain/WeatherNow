package com.mustafa.weathernow.utils

import android.text.format.DateFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

fun Long?.dayFormater(): String {
    return if (this != null) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        calendar.timeInMillis = this * 1000

        DateFormat.format("EEEE", calendar).toString()

    } else ""
}

fun Long?.dateTimeFormater(): String {
    return if (this != null) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        calendar.timeInMillis = this * 1000

        DateFormat.format("E, dd MMM yyyy h:mm aa", calendar).toString()

    } else ""
}


fun Long.dateFormater(): String {
    val calendar = Calendar.getInstance(Locale.getDefault())

    calendar.timeInMillis = this

    return DateFormat.format("E, dd MMM yyyy", calendar).toString()
}


fun Long?.timeFormater(): String {
    return if (this != null) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        calendar.timeInMillis = this * 1000

        DateFormat.format("h:mm aa", calendar).toString()

    } else ""
}


fun Int?.formatAsTimeSegment(): String {
    return if (this != null && this > 9)
        NumberFormat.getInstance().format(this)
    else
        0.format() + NumberFormat.getInstance().format(this)
}

fun Triple<Int, Int, Boolean>.formatAsTimeInMillis(date: Long): Long {
    val hoursOffset = if (this.third) 12 else 0
    val calendar = Calendar.getInstance(Locale.getDefault())

    calendar.timeInMillis = date

    calendar.set(Calendar.HOUR_OF_DAY, first + hoursOffset)
    calendar.set(Calendar.MINUTE, second)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}
