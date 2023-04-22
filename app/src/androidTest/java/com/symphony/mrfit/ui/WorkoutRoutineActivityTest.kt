/*
 *  Created by Team Symphony on 4/21/23, 10:18 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/21/23, 10:18 PM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
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
class WorkoutRoutineActivityTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<WorkoutRoutineActivity>(
        intent = Intent(
            ApplicationProvider.getApplicationContext(),
            WorkoutRoutineActivity::class.java
        )
            .putExtra(WorkoutTemplateActivity.EXTRA_IDENTITY, "test")
    )

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_workoutBuilderActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.routineScreenView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.headerLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.routineNameEditText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.workoutPlaylistEditText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.openPlaylistButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.workoutListView))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.routineScreenButtons))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.startWorkoutButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.newExerciseButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.saveWorkoutButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.deleteWorkoutButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.loadingSpinner))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }
}