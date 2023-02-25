/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
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

    /**
     * Test possible login failures
     */
    @Test
    fun checkFailedLogin() {
        onView(withId(R.id.loginEmail))
            .perform(ViewActions.typeText(TEST_EMAIL), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginPassword))
            .perform(ViewActions.typeText(TEST_PASS), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton))
            .perform(click())

        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }

    /**
     * Test if login works
     */
    /*
    @Test
    fun checkValidLogin() {
        onView(withId(R.id.loginEmail))
            .perform(ViewActions.typeText(REAL_EMAIL), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginPassword))
            .perform(ViewActions.typeText(REAL_PASS), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton))
            .perform(click())
        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }

     */

    companion object {

        const val REAL_EMAIL = "test@symphony.com"
        const val REAL_PASS = "abcd1234"
        const val TEST_EMAIL = "fake_email@symphony.com"
        const val TEST_PASS = "1234abcd"
    }
}