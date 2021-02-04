package com.edipasquale.bitrise.page

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.edipasquale.bitrise.R
import com.edipasquale.bitrise.view.activity.main.MainActivity

class MainPage {

    private lateinit var scenario: ActivityScenario<MainActivity>

    fun launch(): MainPage {
        scenario = launchActivity()

        return this
    }

    fun moveToState(state: Lifecycle.State): MainPage.MainActions {
        scenario.moveToState(state)

        return MainPage.MainActions()
    }

    class MainActions {

        fun assertAuthenticated() {
            onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        }
    }
}