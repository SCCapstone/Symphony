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
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class HomeActivityTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<HomeActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_homeActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.homeScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.userLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.homeProfilePicture))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.homeWelcomeTextView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.homeNameTextView))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.homeScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.settingsCog))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.scheduleButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.pastWorkout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.workoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.historyList))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loadingSpinner))
            .check(matches(not(isDisplayed())))
    }
}