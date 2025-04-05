package com.mustafa.weathernow.data.location.sources.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.mustafa.weathernow.data.location.pojo.FavoriteLocation
import kotlinx.coroutines.flow.first
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

@SmallTest
@RunWith(AndroidJUnit4::class)
class LocatingDaoTest {
    private lateinit var database: LocationDatabase
    private lateinit var dao: LocatingDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocationDatabase::class.java
        ).build()

        dao = database.getLocatingDao()
    }

    @After
    fun cleanEnv() {
        database.close()
    }


    @Test
    fun insertFavoriteLocation_insureItInsertedCorrectly() = runTest {
        val favLocation = FavoriteLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0
        )

        val result = dao.insertFavoriteLocation(favLocation)
        val favoriteLocations = dao.getAllFavoriteLocations()
            .first()

        assertTrue(result > 0)
        assertThat(favoriteLocations.size, `is`(1))
        assertThat(favoriteLocations[0], `is`(favLocation))
    }


    @Test
    fun deleteFavoriteLocation_insureItDeletedCorrectly() = runTest {
        val favLocation = FavoriteLocation(
            id = 1,
            latitude = 30.0,
            longitude = 31.0
        )

        dao.insertFavoriteLocation(favLocation)

        val result = dao.deleteFavoriteLocation(favLocation)
        val favoriteLocations = dao.getAllFavoriteLocations()
            .take(1)
            .toList()
            .first()

        assertTrue(result > 0)
        assertThat(favoriteLocations.size, `is`(0))
    }

}