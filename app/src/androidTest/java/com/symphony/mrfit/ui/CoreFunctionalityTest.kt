/*
 *  Created by Team Symphony on 4/22/23, 6:21 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 6:05 AM
 */

package com.symphony.mrfit.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.symphony.mrfit.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class CoreFunctionalityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    /**
     * Test a run through of the app's core functionality. It should:
     * Log in with the credentials 'demo@email.com' and 'pass123'
     * Start a new workout
     * Make a new workout called "Automated Testing"
     * Add 3 exercises to it, whichever 3 are at the top of the list
     * Start the workout, wait 5 seconds, then finish the workout
     * Return to the home screen after
     * Navigate to and delete this new workout
     * Navigate to the user's profile and log out
     */
    @Test
    fun coreFunctionality() {
        // Wait to automatically move past welcome screen
        Thread.sleep(100)
        Espresso.onView(withId(R.id.layout_loginActivity))
            .check(matches(isDisplayed()))

        // Attempt to log in
        Espresso.onView(withId(R.id.loginEmail))
            .perform(typeText(TEST_EMAIL), closeSoftKeyboard())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.loginPassword))
            .perform(typeText(TEST_PASS), closeSoftKeyboard())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.loginButton))
            .perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.layout_homeActivity))
            .check(matches(isDisplayed()))

        // Navigate to the Template selection screen
        Espresso.onView(withId(R.id.workoutButton))
            .perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.layout_selectionActivity))
            .check(matches(isDisplayed()))

        // Make a new workout and name it
        Espresso.onView(withId(R.id.newWorkoutButton))
            .perform(click())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.routineNameEditText))
            .perform(clearText())
            .perform(typeText(TEST_TEMPLATE), closeSoftKeyboard())

        // Add 3 exercises
        // 1st Exercise
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.newExerciseButton))
            .perform(click())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.exerciseListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.saveTemplateButton))
            .perform(click())
        Thread.sleep(1000)
        // 2nd Exercise
        Espresso.onView(withId(R.id.newExerciseButton))
            .perform(click())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.exerciseListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.saveTemplateButton))
            .perform(click())
        Thread.sleep(1000)
        // 3rd Exercise
        Espresso.onView(withId(R.id.newExerciseButton))
            .perform(click())
        Thread.sleep(500)
        Espresso.onView(withId(R.id.exerciseListView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.saveTemplateButton))
            .perform(click())
        Thread.sleep(1000)

        // Start the workout and finish it after 10 seconds
        Espresso.onView(withId(R.id.startWorkoutButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_currentActivity))
            .check(matches(isDisplayed()))
        Thread.sleep(10000)
        Espresso.onView(withId(R.id.finishWorkoutButton))
            .perform(click())
        Espresso.onView(withText("Good job!")).check(matches(isDisplayed()))
        Espresso.onView(withText("Return Home")).perform(click())

        // Navigate back and delete workout
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.workoutButton))
            .perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.layout_selectionActivity))
            .check(matches(isDisplayed()))
        Espresso.onView(withText(TEST_TEMPLATE))
            .perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.deleteWorkoutButton))
            .perform(click())
        Espresso.pressBack()
        Thread.sleep(1000)

        // Navigate to user profile and log out
        Espresso.onView(withId(R.id.userLayout))
            .perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.layout_profileActivity))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.logoutButton))
            .perform(click())
    }

    companion object {
        const val TEST_EMAIL = "demo@email.com"
        const val TEST_PASS = "pass123"
        const val TEST_TEMPLATE = "Automated Testing"
    }
}