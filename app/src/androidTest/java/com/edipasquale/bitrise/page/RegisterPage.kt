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
import com.edipasquale.bitrise.view.activity.auth.RegisterActivity
import org.hamcrest.Description

class RegisterPage {
    private lateinit var scenario: ActivityScenario<RegisterActivity>

    fun launch(): RegisterPage {
        scenario = launchActivity()

        return this
    }

    fun moveToState(state: Lifecycle.State): RegisterAction {
        scenario.moveToState(state)

        return RegisterAction()
    }

    class RegisterAction {

        fun inputEmail(email: String): RegisterAction {
            onView(withId(R.id.fieldEmail)).perform(ViewActions.typeText(email))

            return this
        }

        fun inputPassword(password: String): RegisterAction {
            onView(withId(R.id.fieldPassword)).perform(ViewActions.typeText(password))

            return this
        }

        fun inputPasswordConfirmation(password: String): RegisterAction {
            onView(withId(R.id.fieldPasswordConfirmation)).perform(ViewActions.typeText(password))

            return this
        }

        fun register(): MainPage.MainActions {
            onView(withId(R.id.buttonRegister)).perform(ViewActions.click())

            return MainPage.MainActions()
        }

        fun validateEmailDisplaysError(displays: Boolean): RegisterAction {
            if (displays)
                onView(withId(R.id.fieldEmail)).check(matches(hasErrorText()))
            else
                onView(withId(R.id.fieldEmail)).check(matches(hasNoErrorText()))

            return this
        }

        fun validatePasswordDisplaysError(displays: Boolean): RegisterAction {
            if (displays)
                onView(withId(R.id.fieldPassword)).check(matches(hasErrorText()))
            else
                onView(withId(R.id.fieldPassword)).check(matches(hasNoErrorText()))


            return this
        }

        fun validatePasswordConfirmationDisplaysError(displays: Boolean): RegisterAction {
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