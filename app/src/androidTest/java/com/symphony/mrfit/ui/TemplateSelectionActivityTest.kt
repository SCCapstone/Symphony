/*
 *  Created by Team Symphony on 4/23/23, 3:46 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/23/23, 3:46 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

class TemplateSelectionActivityTest {


    @get:Rule
    var activityScenarioRule = activityScenarioRule<RoutineSelectionActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_selectionActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.selectionScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.routineListView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.newWorkoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loadingSpinner))
            .check(matches(not(isDisplayed())))
    }
}