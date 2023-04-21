package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class RegisterActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<RegisterActivity>()

    /*tests if register works*/
    @Test
    fun checkValidRegister() {
        onView(withId(R.id.registerEmail))
            .perform(ViewActions.typeText(REAL_EMAIL))
        onView(withId(R.id.registerPassword))
            .perform(ViewActions.typeText(LoginActivityTest.REAL_PASS), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.registerButton))
            .perform(ViewActions.click())
    }

    /*tests if empty email/pass gives error*/
    @Test
    fun checkInvalidRegister() {
        onView(withId(R.id.registerEmail))
            .perform(ViewActions.typeText(TEST_EMAIL))
    }

    companion object {

        const val REAL_EMAIL = "testregister@symphony.com"
        const val REAL_PASS = "abcd1234"
        const val TEST_EMAIL = ""
        const val TEST_PASS = ""
    }
}