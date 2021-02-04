package com.edipasquale.bitrise.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edipasquale.bitrise.KoinTestApp
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.page.RegisterPage
import com.edipasquale.bitrise.repository.auth.AuthRepository
import com.edipasquale.bitrise.repository.main.MainRepository
import com.edipasquale.bitrise.source.session.SessionManager
import com.edipasquale.bitrise.validator.FieldValidator
import com.edipasquale.bitrise.validator.SimpleFieldValidator
import com.edipasquale.bitrise.viewmodel.auth.AuthViewModel
import com.edipasquale.bitrise.viewmodel.main.MainViewModel
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Acceptance test for register feature
 */
@RunWith(AndroidJUnit4::class)
class RegisterPageFlow {

    private lateinit var koinApp: KoinTestApp
    private val _mockedFieldValidator = spyk(SimpleFieldValidator())
    private val _mockedRepository = mockk<AuthRepository>()
    private val _mockedSessionManager = mockk<SessionManager>()
    private val _mockedMainRepository = mockk<MainRepository>()

    @Before
    fun setUp() {
        koinApp = ApplicationProvider.getApplicationContext()

        every { _mockedMainRepository.fetchLatestAdditions(any()) } returns MutableLiveData()

        koinApp.setUpModule(module {
            // Build ViewModel with mocked dependencies
            viewModel { AuthViewModel(get(), get()) }
            // Mock Field Validator
            factory<FieldValidator> { _mockedFieldValidator }
            factory { _mockedRepository }
            single { _mockedSessionManager }
            factory { MainViewModel(_mockedMainRepository, koinApp) }
        })
    }

    @After
    fun shutDown() {
        clearAllMocks()
        koinApp.closeKoin()
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
            .inputEmail("valid@email.domain")
            .inputPassword("ValidPassword.1234")
            .inputPasswordConfirmation("ValidPassword.1234")
            .register()
            .assertAuthenticated()
    }

    @Test
    fun testInvalidEmail() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("someemail")
            .apply {
                register()
            }
            .validateEmailDisplaysError(true)
    }

    @Test
    fun testInvalidPassword() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("valid@email.domain")
            .inputPassword("invalidpassword")
            .inputPasswordConfirmation("invalidpassword")
            .apply {
                register()
            }
            .validateEmailDisplaysError(false)
            .validatePasswordDisplaysError(true)
            .validatePasswordConfirmationDisplaysError(false)
    }

    @Test
    fun testPasswordsDontMatch() {
        mockSession(null)

        val page = RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("valid@email.domain")
            .inputPassword("ValidPassword.1234")
            .inputPasswordConfirmation("something")

        page.register()
        page.validateEmailDisplaysError(false)
            .validatePasswordDisplaysError(false)
            .validatePasswordConfirmationDisplaysError(true)
    }

    @Test
    fun testEmptyEmail() {
        mockSession(null)

        RegisterPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .apply {
                register()
            }
            .validateEmailDisplaysError(true)
    }

    private fun mockSession(user: User?) {
        every { _mockedSessionManager.getUser() } returns user
    }
}