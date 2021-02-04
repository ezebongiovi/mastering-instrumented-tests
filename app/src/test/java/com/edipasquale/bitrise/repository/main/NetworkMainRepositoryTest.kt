package com.edipasquale.bitrise.repository.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.dto.api.APIResponse
import com.edipasquale.bitrise.ext.getOrAwaitValue
import com.edipasquale.bitrise.source.main.MainLocalSource
import com.edipasquale.bitrise.source.main.MainNetworkSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class NetworkMainRepositoryTest {

    private val _mockedNetworkSource = mockk<MainNetworkSource>()
    private val _mockedLocalSource = mockk<MainLocalSource>()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchLatestAdditions() = testCoroutineScope.runBlockingTest {
        val mockedCall = mockk<Call<APIResponse<List<RecentAnimeEntity>>>>()
        every { _mockedNetworkSource.fetchLatestAdditions() } returns mockedCall
        every { _mockedLocalSource.fetchLatestAdditions() } returns MutableLiveData<PagedList<RecentAnimeEntity>>().apply {
            value = null
        }

        every { mockedCall.execute() } returns Response.success(APIResponse(data = emptyList()))

        val repository =
            NetworkMainRepository(_mockedNetworkSource, _mockedLocalSource, testCoroutineDispatcher)

        repository.fetchLatestAdditions(true).getOrAwaitValue()

        verify(exactly = 1) { _mockedLocalSource.fetchLatestAdditions() }
        verify(exactly = 1) { _mockedNetworkSource.fetchLatestAdditions() }
    }

    @Test
    fun fetchLatestAdditionsError() = testCoroutineScope.runBlockingTest {
        val mockedCall = mockk<Call<APIResponse<List<RecentAnimeEntity>>>>()
        every { _mockedNetworkSource.fetchLatestAdditions() } returns mockedCall
        every { mockedCall.execute() } returns Response.error(400, "".toResponseBody(null))
        every { _mockedLocalSource.fetchLatestAdditions() } returns MutableLiveData<PagedList<RecentAnimeEntity>>().apply {
            value = null
        }

        val repository =
            NetworkMainRepository(_mockedNetworkSource, _mockedLocalSource, testCoroutineDispatcher)

        repository.fetchLatestAdditions(false).getOrAwaitValue()

        verify(exactly = 0) { _mockedNetworkSource.fetchLatestAdditions() }
        verify(exactly = 1) { _mockedLocalSource.fetchLatestAdditions() }
    }
}