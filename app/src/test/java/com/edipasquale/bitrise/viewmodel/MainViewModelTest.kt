package com.edipasquale.bitrise.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.source.SimpleAuthSource
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
        val source = mockk<SimpleAuthSource>()
        val viewModel = MainViewModel(validator, source)

        val email = "email@email.com"
        val password = "password"
        val passwordConfirmation = "passwords"

        viewModel.register(email, password, passwordConfirmation)

        viewModel.authResult.observeForever { model ->
            assertFalse(model.isSuccess())

            assertEquals(model.error, MainModel.Result.ERROR_PASSWORD_MATCH)
        }
    }

    @Test
    fun testPasswordsMatch() {
        val validator = mockk<FieldValidator>()
        val source = mockk<SimpleAuthSource>()
        val viewModel = MainViewModel(validator, source)
        val liveData = MutableLiveData<MainModel>()

        val email = "email@email.com"
        val password = "password"
        val passwordConfirmation = "password"

        liveData.postValue(MainModel(data = User(email)))

        every { source.register(any()) }.returns(liveData)

        every { validator.validateField(any(), any()) }.returns(MainModel.Result.SUCCESS)

        viewModel.register(email, password, passwordConfirmation)

        viewModel.authResult.observeForever { model ->
            assertTrue(model.isSuccess())

            assertNull(model.error)
        }
    }
}