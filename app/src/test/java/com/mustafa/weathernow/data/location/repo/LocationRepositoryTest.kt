package com.mustafa.weathernow.data.location.repo

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.sources.local.FakeLocationLocalDatasource
import com.mustafa.weathernow.data.location.sources.local.ILocationLocalDatasource
import com.mustafa.weathernow.data.location.sources.remote.ILocationRemoteDataSource
import com.mustafa.weathernow.data.location.sources.shared_prefs.LocationSharedPrefs
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationRepositoryTest {

    lateinit var remoteLocationDatasource: ILocationRemoteDataSource
    lateinit var localLocationDatasource: ILocationLocalDatasource
    lateinit var locationSharedPrefs: LocationSharedPrefs
    lateinit var locationRepository: ILocationRepository

    @Before
    fun setup() {
        remoteLocationDatasource = mockk()
        localLocationDatasource = FakeLocationLocalDatasource()
        locationSharedPrefs = mockk()

        locationRepository = LocationRepository(
            remoteLocationDatasource,
            localLocationDatasource,
            locationSharedPrefs
        )
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

        val insertionResult = locationRepository.insertAlert(alertLocation)
        val alerts = locationRepository
            .getAlerts()
            .take(1)
            .toList()
            .first()

        assertTrue(insertionResult > 0)
        assertThat(alerts.size, `is`(1))
        assertThat(alerts[0], `is`(alertLocation))
    }

    @Test
    fun deleteAlert_insureItDeletedCorrectly() = runTest() {
        val alertLocation = AlertLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0,
            startTime = 10000,
            alertType = "notification"
        )

        locationRepository.insertAlert(alertLocation)
        val deletionResult = locationRepository.deleteAlert(alertLocation)
        val alerts = locationRepository
            .getAlerts()
            .take(1)
            .toList()
            .first()

        assertTrue(deletionResult > 0)
        assertThat(alerts.size, `is`(0))
    }

}