package com.edipasquale.bitrise.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.dto.Either
import com.edipasquale.bitrise.dto.User
import com.edipasquale.bitrise.model.MainModel
import com.edipasquale.bitrise.source.AuthSource
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.*
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
        val authDto = AuthDTO("email", "pass")

        // Mocks success response
        sourceResponse.postValue(Either.Data(User(authDto.email, authDto.password)))

        every {
            source.register(any())
        }.returns(sourceResponse)

        repository.register(authDto).observeForever { response ->
            assertTrue(response.isSuccess())

            assertEquals(authDto.email, response.data!!.email)
            assertEquals(authDto.password, response.data!!.password)
        }
    }

    @Test
    fun testErrorCase() {
        val source = mockk<AuthSource>()
        val sourceResponse = MutableLiveData<Either<User, Int>>()
        val repository = SimpleAuthRepository(source)
        val authDto = AuthDTO("email", "pass")

        // Mocks success response
        sourceResponse.postValue(Either.Error(MainModel.ERROR_EMAIL_FORMAT))

        every {
            source.register(any())
        }.returns(sourceResponse)

        repository.register(authDto).observeForever { response ->
            assertFalse(response.isSuccess())

            assertEquals(MainModel.ERROR_EMAIL_FORMAT, response.error)
        }
    }
}