package com.edipasquale.bitrise.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.repository.AuthRepository
import com.edipasquale.bitrise.validator.FieldValidator
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testPasswordsDontMatch() {
        val validator = mockk<FieldValidator>()
        val repository = mockk<AuthRepository>()
        val viewModel = MainViewModel(validator, repository)

        val email = "email@email.com"
        val password = "password"
        val passwordConfirmation = "passwords"

        viewModel.register(email, password, passwordConfirmation)

        viewModel.authResult.observeForever { model ->
            assertFalse(model.isSuccess())

            assertEquals(model.error, MainModel.ERROR_PASSWORD_MATCH)
        }
    }

    @Test
    fun testPasswordsMatchWithAllValidationsSuccedded() {
        val validator = mockk<FieldValidator>()
        val repository = mockk<AuthRepository>()
        val viewModel = MainViewModel(validator, repository)
        val liveData = MutableLiveData<MainModel>()

        val email = "email@email.com"
        val password = "password"
        val passwordConfirmation = "password"

        liveData.postValue(MainModel(data = User(email, password)))

        every { repository.register(any()) }.returns(liveData)

        every { validator.validateField(any(), any()) }.returns(MainModel.SUCCESS)

        viewModel.register(email, password, passwordConfirmation)

        viewModel.authResult.observeForever { model ->
            assertTrue(model.isSuccess())

            assertNull(model.error)
        }
    }

    @Test
    fun testPasswordsMatchWithPasswordError() {
        val validator = mockk<FieldValidator>()
        val repository = mockk<AuthRepository>()
        val viewModel = MainViewModel(validator, repository)

        // Mock email validation to success
        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_EMAIL)
        }.returns(MainModel.SUCCESS)

        // Mock password validation to error
        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_PASSWORD)
        }.returns(MainModel.ERROR_PASSWORD_FORMAT)

        viewModel.register("test", "test", "test")

        viewModel.authResult.observeForever { model ->
            assertFalse(model.isSuccess())

            assertEquals(MainModel.ERROR_PASSWORD_FORMAT, model.error)
        }
    }

    @Test
    fun testPasswordsMatchWithEmailError() {
        val validator = mockk<FieldValidator>()
        val repository = mockk<AuthRepository>()
        val viewModel = MainViewModel(validator, repository)

        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_EMAIL)
        }.returns(MainModel.ERROR_EMAIL_FORMAT)

        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_PASSWORD)
        }.returns(MainModel.SUCCESS)

        viewModel.register("test", "test", "test")

        viewModel.authResult.observeForever { model ->
            assertFalse(model.isSuccess())

            assertEquals(MainModel.ERROR_EMAIL_FORMAT, model.error)
        }
    }

    @Test
    fun testPasswordsMatchWithEmailAndPasswordError() {
        val validator = mockk<FieldValidator>()
        val repository = mockk<AuthRepository>()
        val viewModel = MainViewModel(validator, repository)

        // Mocks password validation to error
        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_PASSWORD)
        }.returns(MainModel.ERROR_PASSWORD_FORMAT)

        // Mocks email validation to error
        every {
            validator.validateField(any(), FieldValidator.FieldType.FIELD_EMAIL)
        }.returns(MainModel.ERROR_EMAIL_FORMAT)

        viewModel.register("test", "test", "test")

        viewModel.authResult.observeForever { model ->
            assertFalse(model.isSuccess())

            // Email is being validated first. Since the email is requested first
            assertEquals(MainModel.ERROR_EMAIL_FORMAT, model.error)
        }
    }
}