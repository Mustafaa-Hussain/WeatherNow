package com.mustafa.weathernow.alert.view_model

import com.mustafa.weathernow.data.location.pojo.AlertLocation
import com.mustafa.weathernow.data.location.repo.ILocationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherAlertsViewModelTest {
    private lateinit var locationRepository: ILocationRepository
    private lateinit var weatherAlertsViewModel: WeatherAlertsViewModel

    lateinit var alertLocation: AlertLocation

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        locationRepository = mockk(relaxed = true)
        weatherAlertsViewModel = WeatherAlertsViewModel(locationRepository)

        alertLocation = AlertLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0,
            startTime = 1000000,
            alertType = "notification"
        )

    }

    @Test
    fun saveAlert_insureAlertIsSaved() = runTest {

        coEvery { locationRepository.insertAlert(alertLocation) } returns 1
        weatherAlertsViewModel.saveAlert(alertLocation)

        advanceUntilIdle()

        assertTrue(weatherAlertsViewModel.saveStatus.first())
    }

    @Test
    fun deleteAlert_insureAlertIsDeleted() = runTest {

        coEvery { locationRepository.deleteAlert(alertLocation) } returns 1

        weatherAlertsViewModel.deleteAlert(alertLocation)

        advanceUntilIdle()

        assertTrue(weatherAlertsViewModel.deletionStatus.value)
    }

}


@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}