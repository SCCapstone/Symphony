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
import org.hamcrest.CoreMatchers.not
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
        Espresso.onView(withId(R.id.layout_workoutBuilderActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.routineScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.headerLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.routineNameEditText))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.workoutPlaylistEditText))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.openPlaylistButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.workoutListView))
            .check(matches(not(isDisplayed())))

        Espresso.onView(withId(R.id.routineScreenButtons))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.startWorkoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.newExerciseButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.saveWorkoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.deleteWorkoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loadingSpinner))
            .check(matches(not(isDisplayed())))
    }
}