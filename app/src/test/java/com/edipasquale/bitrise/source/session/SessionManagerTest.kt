package com.edipasquale.bitrise.source.session

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.edipasquale.bitrise.dto.User
import com.google.gson.Gson
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SessionManagerTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val _user = User("Email")
    private val _mockedContext = mockk<Context>()
    private val _mockedSharedPreferences = mockk<SharedPreferences>()
    private val _mockedEditor = mockk<SharedPreferences.Editor>()

    @Before
    fun setUp() {
        every { _mockedContext.getSharedPreferences(any(), any()) } returns _mockedSharedPreferences
        every { _mockedSharedPreferences.getString(any(), any()) } returns Gson().toJson(_user)
        every { _mockedSharedPreferences.edit() } returns _mockedEditor
        every { _mockedEditor.putString(any(), any()) } returns _mockedEditor
        every { _mockedEditor.apply() } just runs
    }

    @Test
    fun saveUser() {
        val sessionManager = SessionManager(_mockedContext)

        sessionManager.saveUser(_user)

        verify(exactly = 1) { _mockedEditor.putString(any(), Gson().toJson(_user)) }
        verify(exactly = 1) { _mockedEditor.apply() }
    }

    @Test
    fun getUser() {
        val sessionManager = SessionManager(_mockedContext)

        sessionManager.getUser()

        verify(exactly = 1) { _mockedSharedPreferences.getString(any(), any()) }
        assertEquals(_user, sessionManager.getUser())
    }
}