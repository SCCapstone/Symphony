/*
 *  Created by Team Symphony on 4/22/23, 3:13 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 2:45 AM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class WorkoutTemplateActivityTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<WorkoutTemplateActivity>(
        intent = Intent(
            ApplicationProvider.getApplicationContext(),
            WorkoutTemplateActivity::class.java
        )
            .putExtra(WorkoutRoutineActivity.EXTRA_ROUTINE, "test")
            .putExtra(WorkoutTemplateActivity.EXTRA_IDENTITY, "test")
            .putExtra(WorkoutTemplateActivity.EXTRA_EXERCISE, "test")
            .putExtra(WorkoutTemplateActivity.EXTRA_LIST, ArrayList<String>())
    )

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_exerciseBuilderActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.templateScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.exerciseCardView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.editDuration))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.editDistance))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.repsAndSetsLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.editReps))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.editSets))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.deleteTemplateButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.saveTemplateButton))
            .check(matches(isDisplayed()))
    }
}