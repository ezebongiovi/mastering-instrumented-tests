package com.edipasquale.bitrise.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.dto.core.Either
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.ERROR_EMAIL_FORMAT
import com.edipasquale.bitrise.source.auth.AuthSource
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SimpleAuthRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testSuccessCase() {
        val source = mockk<AuthSource>()
        val sourceResponse = MutableLiveData<Either<User, Int>>()
        val repository = SimpleAuthRepository(source)
        val authDto = EmailPasswordAuth("email", "pass")

        // Mocks success response
        sourceResponse.postValue(Either.Data(User(authDto.email)))

        every {
            source.register(any())
        }.returns(sourceResponse)

        repository.register(authDto).observeForever { response ->
            assertTrue(response.isSuccess())

            assertEquals(authDto.email, response.data!!.email)
        }
    }

    @Test
    fun testErrorCase() {
        val source = mockk<AuthSource>()
        val sourceResponse = MutableLiveData<Either<User, Int>>()
        val repository = SimpleAuthRepository(source)
        val authDto = EmailPasswordAuth("email", "pass")

        // Mocks success response
        sourceResponse.postValue(Either.Error(ERROR_EMAIL_FORMAT))

        every {
            source.register(any())
        }.returns(sourceResponse)

        repository.register(authDto).observeForever { response ->
            assertFalse(response.isSuccess())

            assertEquals(ERROR_EMAIL_FORMAT, response.error)
        }
    }
}