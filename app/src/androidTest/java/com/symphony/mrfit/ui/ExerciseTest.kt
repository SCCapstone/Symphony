/*
 *  Created by Team Symphony on 4/22/23, 3:13 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 2:45 AM
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
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ExerciseTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<ExerciseActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_exerciseSelectionActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.exerciseScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.exerciseListView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.exerciseSearchTextView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.exerciseSearchEditText))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.exerciseSearchButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.button2))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loadingSpinner))
            .check(matches(not(isDisplayed())))
    }
}