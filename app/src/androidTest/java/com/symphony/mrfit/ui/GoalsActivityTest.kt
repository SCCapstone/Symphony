/*
 *  Created by Team Symphony on 4/23/23, 3:46 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/23/23, 3:11 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.junit.Rule
import org.junit.Test

class GoalsActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<GoalsActivity>()

    //TODO: added simple tests, worked on mine but check if needs more
    //tests if the activity is displayed and visible to user
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_goalsActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //tests if components are visible
    @Test
    fun checkViewVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.goalsTitleTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.goalsList))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}

