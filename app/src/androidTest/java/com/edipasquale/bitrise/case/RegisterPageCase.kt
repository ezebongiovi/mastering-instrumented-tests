package com.edipasquale.bitrise.case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edipasquale.bitrise.KoinTestApp
import com.edipasquale.bitrise.db.entity.RecentAnimeEntity
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.page.MainPage
import com.edipasquale.bitrise.page.RegisterPage
import com.edipasquale.bitrise.repository.auth.AuthRepository
import com.edipasquale.bitrise.repository.main.MainRepository
import com.edipasquale.bitrise.source.session.SessionManager
import com.edipasquale.bitrise.validator.FieldValidator
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.auth.AuthViewModel
import com.edipasquale.bitrise.viewmodel.main.MainViewModel
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class RegisterPageCase {

    private lateinit var koinApp: KoinTestApp
    private val _mockedSessionManager = mockk<SessionManager>()
    private val _mockedRepository = mockk<AuthRepository>()
    private val _mockedMainRepository = mockk<MainRepository>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        koinApp = ApplicationProvider.getApplicationContext()

        every { _mockedMainRepository.fetchLatestAdditions(any()) } returns MutableLiveData()

        koinApp.setUpModule(module {
            viewModel { AuthViewModel(get(), get()) }

            factory<FieldValidator> { SimpleFieldValidator() }
            factory { _mockedRepository }
            factory { _mockedSessionManager }
            factory { MainViewModel(_mockedMainRepository, koinApp) }
        })
    }

    @After
    fun shutDown() {
        clearAllMocks()
        koinApp.closeKoin()
    }

    @Test
    fun testInvalidEmail() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("sarasa")
            .apply {
                register()
            }
            .validateEmailDisplaysError(true)
    }

    @Test
    fun testPasswordLengthError() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("sarasa@sarasa.sarasa")
            .inputPassword("sar")
            .inputPasswordConfirmation("sar")
            .apply {
                register()
            }
            .validatePasswordDisplaysError(true)
    }

    @Test
    fun testPasswordsDontMatch() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("sarasa@sarasa.sarasa")
            .inputPassword("Sarasa.2020")
            .inputPasswordConfirmation("Sarasa.2019")
            .apply {
                register()
            }
            .validatePasswordConfirmationDisplaysError(true)
    }

    @Test
    fun testPasswordInvalid() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("sarasa@sarasa.sarasa")
            .inputPassword("Sarasa")
            .inputPasswordConfirmation("Sarasa")
            .apply {
                register()
            }
            .validatePasswordDisplaysError(true)
    }

    @Test
    fun redirectedWhenAuthenticated() {
        mockSession(User(""))

        every { _mockedMainRepository.fetchLatestAdditions(any()) } returns MutableLiveData()

        RegisterPage()
            .launch()

        MainPage.MainActions().assertAuthenticated()
    }

    @Test
    fun testSuccessCase() {
        mockSession(null)

        every { _mockedRepository.register(any()) } returns liveData {
            emit(MainModel(data = User("")))
        }

        every { _mockedSessionManager.saveUser(any()) } answers {
            mockSession(User(""))
        }

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("sarasa@sarasa.sarasa")
            .inputPassword("Sarasa.2020")
            .inputPasswordConfirmation("Sarasa.2020")
            .register()
            .assertAuthenticated()
    }

    private fun mockSession(user: User?) {
        every { _mockedSessionManager.getUser() } returns user
    }
}

