package com.edipasquale.bitrise.source.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edipasquale.bitrise.db.AnimeDao
import com.edipasquale.bitrise.db.AppDatabase
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.ext.getOrAwaitValueAndroid
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainLocalSourceTest {
    private lateinit var _source: MainLocalSource
    private lateinit var _database: AppDatabase
    private lateinit var _dao: AnimeDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        _database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        _dao = spyk(_database.dao())
        _source = MainLocalSource(_dao)
    }

    @After
    fun shutDown() {
        _database.close()
    }

    @Test
    fun fetchLatestAdditions() {
        _source.fetchLatestAdditions().getOrAwaitValueAndroid()

        verify(exactly = 1) { _dao.getAllRecents() }
    }

    @Test
    fun insert() {
        assertTrue(_source.fetchLatestAdditions().getOrAwaitValueAndroid().isEmpty())
        val entity = RecentAnimeEntity(
            id = "1",
            cover = "Cover",
            title = "Title",
            formattedDate = "some"
        )

        val insertedList = listOf(entity)

        _source.insertRecents(insertedList)

        verify(exactly = 1) { _dao.insertRecents(insertedList) }

        assertTrue(_source.fetchLatestAdditions().getOrAwaitValueAndroid().isNotEmpty())
        assertEquals(entity, _source.fetchLatestAdditions().getOrAwaitValueAndroid()[0])
    }
}