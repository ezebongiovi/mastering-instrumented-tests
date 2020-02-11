package com.edipasquale.bitrise.page

import android.view.View
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.edipasquale.bitrise.R
import com.edipasquale.bitrise.view.MainActivity
import org.hamcrest.Description

class MainPage {
    private lateinit var scenario: ActivityScenario<MainActivity>

    fun launch(): MainPage {
        scenario = launchActivity()

        return this
    }

    fun moveToState(state: Lifecycle.State): MainPageAction {
        scenario.moveToState(state)

        return MainPageAction()
    }

    class MainPageAction {

        fun inputEmail(email: String): MainPageAction {
            onView(withId(R.id.fieldEmail)).perform(ViewActions.typeText(email))

            return this
        }

        fun inputPassword(password: String): MainPageAction {
            onView(withId(R.id.fieldPassword)).perform(ViewActions.typeText(password))

            return this
        }

        fun inputPasswordConfirmation(password: String): MainPageAction {
            onView(withId(R.id.fieldPasswordConfirmation)).perform(ViewActions.typeText(password))

            return this
        }

        fun register(): MainPageAction {
            onView(withId(R.id.buttonRegister)).perform(ViewActions.click())

            return this
        }

        fun validateEmailDisplaysError(displays: Boolean): MainPageAction {
            if (displays)
                onView(withId(R.id.fieldEmail)).check(matches(hasErrorText()))
            else
                onView(withId(R.id.fieldEmail)).check(matches(hasNoErrorText()))

            return this
        }

        fun validatePasswordDisplaysError(displays: Boolean): MainPageAction {
            if (displays)
                onView(withId(R.id.fieldPassword)).check(matches(hasErrorText()))
            else
                onView(withId(R.id.fieldPassword)).check(matches(hasNoErrorText()))


            return this
        }

        fun validatePasswordConfirmationDisplaysError(displays: Boolean): MainPageAction {
            if (displays)
                onView(withId(R.id.fieldPasswordConfirmation)).check(matches(hasErrorText()))
            else
                onView(withId(R.id.fieldPasswordConfirmation)).check(matches(hasNoErrorText()))

            return this
        }

        private fun hasNoErrorText() =
            object : BoundedMatcher<View, EditText>(EditText::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText("has no error text: ")
                }

                override fun matchesSafely(view: EditText): Boolean = view.error == null
            }

        private fun hasErrorText() =
            object : BoundedMatcher<View, EditText>(EditText::class.java) {

                override fun describeTo(description: Description) {
                    description.appendText("has no error text: ")
                }

                override fun matchesSafely(view: EditText): Boolean = view.error != null
            }
    }
}