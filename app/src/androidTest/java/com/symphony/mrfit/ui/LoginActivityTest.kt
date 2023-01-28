package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LoginActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }

    /**
     * Test if all the components are visible
     */
    @Test
    fun checkViewVisibility() {
        onView(withId(R.id.loginEmail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.loginPassword))
            .check(matches(isDisplayed()))

        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.googleButton))
            .check(matches(isDisplayed()))

        onView(withId(R.id.toRegisterTextView))
            .check(matches(isDisplayed()))
    }

    companion object {

        val STRING_TO_BE_TYPED = "Espresso"
    }
}