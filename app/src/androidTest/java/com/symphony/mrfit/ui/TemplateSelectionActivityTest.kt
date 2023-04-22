/*
 *  Created by Team Symphony on 4/21/23, 10:18 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/21/23, 9:24 PM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class TemplateSelectionActivityTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<RoutineSelectionActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_selectionActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.selectionScreenView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.routineListView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.newWorkoutButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.loadingSpinner))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }
}