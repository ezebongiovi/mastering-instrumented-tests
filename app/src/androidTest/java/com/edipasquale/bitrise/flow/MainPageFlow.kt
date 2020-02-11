package com.edipasquale.bitrise.flow

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edipasquale.bitrise.KoinTestApp
import com.edipasquale.bitrise.koin.AppInjector
import com.edipasquale.bitrise.page.MainPage
import com.edipasquale.bitrise.view.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Acceptance test for register feature
 */
@RunWith(AndroidJUnit4::class)
class MainPageFlow {

    private lateinit var koinApp: KoinTestApp

    @Before
    fun setUp() {
        koinApp = ApplicationProvider.getApplicationContext()

        koinApp.setUpModule(AppInjector().appModule)
    }

    @After
    fun shutDown() {
        koinApp.closeKoin()
    }

    @Test
    fun testSuccessCase() {
        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("valid@email.domain")
            .inputPassword("ValidPassword.123")
            .inputPasswordConfirmation("ValidPassword.123")
            .register()
            .validateTheresNoError()
    }

    @Test
    fun testInvalidEmail() {
        MainPage()
            .launch()
            .moveToState(Lifecycle.State.RESUMED)
            .inputEmail("someemail")
            .register()
            .validateEmailDisplaysError()
    }
}