package com.edipasquale.bitrise.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.edipasquale.bitrise.dto.EmailPasswordAuth
import com.edipasquale.bitrise.source.auth.FirebaseAuthSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.dsl.module
import org.koin.test.KoinTestRule

@RunWith(JUnit4::class)
class FirebaseAuthSourceTest {
    private val mockFirebaseAuth = mockk<FirebaseAuth>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(module {
            single { mockFirebaseAuth }
        })
    }

    @Before
    fun setUp() {
        val mock = mockk<Task<AuthResult>>()
        every { mockFirebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mock
        every { mock.addOnFailureListener(any()) } returns mock
        every { mock.addOnSuccessListener(any()) } returns mock
    }

    @Test
    fun firebaseAuthGetsCalledWithProperData() {
        val source = FirebaseAuthSource()
        val authDTO = EmailPasswordAuth(
            "email",
            "pass"
        )

        source.register(authDTO)

        verify(exactly = 1) {
            mockFirebaseAuth.createUserWithEmailAndPassword(authDTO.email, authDTO.password)
        }
    }
}