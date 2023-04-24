/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:50 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.junit.Rule
import org.junit.Test

class PostWorkoutActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<PostWorkoutActivity>()

    //tests if the activity is displayed and visible to user
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_postWorkoutActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //tests if components are visible
    @Test
    fun checkViewVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.postWorkoutTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.postWorkoutTime))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        /*Espresso.onView(ViewMatchers.withId(R.id.gotoGoalsButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.returnHomeButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))*/

    }
}