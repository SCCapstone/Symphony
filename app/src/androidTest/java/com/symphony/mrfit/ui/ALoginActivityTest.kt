/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.junit.Rule
import org.junit.Test

class ALoginActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<LoginActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_loginActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the components are visible
     */
    @Test
    fun checkViewVisibility() {
        Espresso.onView(withId(R.id.loginEmail))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loginPassword))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.googleButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.toRegisterTextView))
            .check(matches(isDisplayed()))
    }

    /**
     * Test possible login failures
     */
    @Test
    fun checkFailedLogin() {
        Espresso.onView(withId(R.id.loginEmail))
            .perform(typeText(TEST_EMAIL), closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginPassword))
            .perform(typeText(TEST_PASS), closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginButton))
            .perform(click())

        Espresso.onView(withId(R.id.layout_loginActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if login works
     */
    @Test
    fun checkValidLogin() {
        Espresso.onView(withId(R.id.loginEmail))
            .perform(typeText(REAL_EMAIL), closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginPassword))
            .perform(typeText(REAL_PASS), closeSoftKeyboard())
        Espresso.onView(withId(R.id.loginButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_loginActivity))
            .check(matches(isDisplayed()))
    }

    companion object {

        const val REAL_EMAIL = "demo@symphony.com"
        const val REAL_PASS = "pass123"
        const val TEST_EMAIL = "fake_email@symphony.com"
        const val TEST_PASS = "1234abcd"
    }
}