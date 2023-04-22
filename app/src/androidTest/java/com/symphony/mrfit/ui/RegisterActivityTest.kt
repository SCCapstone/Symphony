/*
 *  Created by Team Symphony on 4/22/23, 6:21 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 6:05 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class RegisterActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<RegisterActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_registerActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the components are visible
     */
    @Test
    fun checkViewVisibility() {
        Espresso.onView(withId(R.id.registerEmail))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.registerPassword))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.confirmPassword))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.registerButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.toLoginTextView))
            .check(matches(isDisplayed()))
    }
}