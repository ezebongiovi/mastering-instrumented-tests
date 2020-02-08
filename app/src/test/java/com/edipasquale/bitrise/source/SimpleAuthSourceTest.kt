package com.edipasquale.bitrise.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.edipasquale.bitrise.dto.AuthDTO
import com.edipasquale.bitrise.dto.Either
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SimpleAuthSourceTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testUserBuildsWithInputEmail() {
        val source = SimpleAuthSource()
        val authDTO = AuthDTO(
            "email",
            "pass"
        )

        source.register(authDTO).observeForever { model ->
            assertTrue(model is Either.Data)

            val data = model as Either.Data

            assertEquals(authDTO.email, data.data.email)
            assertEquals(authDTO.password, data.data.password)
        }
    }
}