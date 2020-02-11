package com.edipasquale.bitrise.case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edipasquale.bitrise.KoinTestApp
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.*
import com.edipasquale.bitrise.page.MainPage
import com.edipasquale.bitrise.view.MainActivity
import com.edipasquale.bitrise.viewmodel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class MainPageCase {

    private lateinit var koinApp: KoinTestApp
    private val mockViewModel = spyk(mockk<MainViewModel>(), recordPrivateCalls = true)


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        koinApp = ApplicationProvider.getApplicationContext()

        koinApp.setUpModule(module {
            viewModel {
                mockViewModel
            }
        })
    }

    @After
    fun shutDown() {
        koinApp.closeKoin()
    }

    @Test
    fun testInvalidEmail() {
        mockViewModelResponseToError(ERROR_EMAIL_FORMAT)

        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .validateEmailDisplaysError()
    }

    @Test
    fun testPasswordLengthError() {
        mockViewModelResponseToError(ERROR_PASSWORD_LENGTH)

        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .validatePasswordDisplaysError()
    }

    @Test
    fun testPasswordsDontMatch() {
        mockViewModelResponseToError(ERROR_PASSWORD_MATCH)

        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .validatePasswordConfirmationDisplaysError()
    }

    @Test
    fun testPasswordInvalid() {
        mockViewModelResponseToError(ERROR_PASSWORD_FORMAT)

        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .validatePasswordDisplaysError()
    }

    @Test
    fun testSuccessCase() {
        every { mockViewModel.authResult }.answers {
            val authResult = MediatorLiveData<MainModel>()

            // Mock response to email format error
            authResult.postValue(MainModel(data = User("", "")))


            authResult
        }

        every { mockViewModel.register(any(), any(), any()) }.answers {
            nothing
        }

        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .validateTheresNoError()
    }

    private fun mockViewModelResponseToError(errorCode: Int) {
        every { mockViewModel.authResult }.answers {
            val authResult = MediatorLiveData<MainModel>()

            // Mock response to email format error
            authResult.postValue(MainModel(error = errorCode))

            authResult
        }

        every { mockViewModel.register(any(), any(), any()) }.answers {
            nothing
        }
    }
}

