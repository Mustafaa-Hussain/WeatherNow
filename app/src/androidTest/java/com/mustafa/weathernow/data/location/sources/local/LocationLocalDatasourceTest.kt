package com.mustafa.weathernow.data.location.sources.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mustafa.weathernow.data.location.pojo.AlertLocation
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LocationLocalDatasourceTest {

    private lateinit var locationLocalDatasource: LocationLocalDatasource
    private lateinit var locationDao: LocatingDao
    private lateinit var locationDatabase: LocationDatabase

    @Before
    fun setup() {
        //setup room
        locationDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocationDatabase::class.java
        )
            .build()

        locationDao = locationDatabase.getLocatingDao()

        locationLocalDatasource = LocationLocalDatasource(locationDao)
    }

    @After
    fun cleanEnv() {
        locationDatabase.close()
    }

    @Test
    fun insertAlert_insureItInsertedCorrectly() = runTest {
        val alertLocation = AlertLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0,
            startTime = 10000,
            alertType = "notification"
        )

        val insertionResult = locationLocalDatasource.insertAlert(alertLocation)

        val alerts = locationLocalDatasource.getAllAlerts()
            .take(1)
            .toList()
            .first()

        assertTrue(insertionResult > 0)
        assertThat(alerts.size, `is`(1))
        assertThat(alerts[0], `is`(alertLocation))
    }

    @Test
    fun deleteAlertById_insureItDeletedCorrectly() = runTest {
        val alertLocation = AlertLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0,
            startTime = 10000,
            alertType = "notification"
        )

        locationLocalDatasource.insertAlert(alertLocation)

        val deletionResult = locationLocalDatasource
            .deleteAlert(alertLocation.id.toInt())

        val alerts = locationLocalDatasource.getAllAlerts()
            .take(1)
            .toList()
            .first()

        assertTrue(deletionResult > 0)
        assertThat(alerts.size, `is`(0))
    }

}