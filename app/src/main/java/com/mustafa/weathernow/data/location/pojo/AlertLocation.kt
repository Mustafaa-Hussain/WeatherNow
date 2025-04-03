package com.mustafa.weathernow.data.location.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val longitude: Double,
    val latitude: Double,
    val startTime: Long,
    val alertType: String //notification or overlay alert
)