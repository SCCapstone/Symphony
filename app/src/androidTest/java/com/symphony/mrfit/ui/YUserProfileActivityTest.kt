/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.hamcrest.CoreMatchers.not
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test

@FixMethodOrder
class YUserProfileActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<UserProfileActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(withId(R.id.layout_profileActivity))
            .check(matches(isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.profileScreenView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.profilePicture))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.profileNameTextView))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.editProfileButton))
            .check(matches(not(isDisplayed())))

        Espresso.onView(withId(R.id.heightLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.weightLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.ageLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.stuffLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.goalsButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.notificationsButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.historyButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.customExercisesButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.settingLayout))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.notificationToggle))
            .check(matches(not(isDisplayed())))

        Espresso.onView(withId(R.id.logoutButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.deleteButton))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.loadingSpinner))
            .check(matches(not(isDisplayed())))
    }

    /**
     * Test if the Notifications button navigates properly
     */
    @Test
    fun notificationsButtonTest() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.notificationsButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_notificationLogActivity))
            .check(matches(isDisplayed()))
        Espresso.pressBack()
    }

    /**
     * Test if the Goals button navigates properly
     */
    @Test
    fun goalsButtonTest() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.goalsButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_goalsActivity))
            .check(matches(isDisplayed()))
        Espresso.pressBack()
    }

    /**
     * Test if the History button navigates properly
     */
    @Test
    fun historyButtonTest() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.historyButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_workoutHistoryActivity))
            .check(matches(isDisplayed()))
        Espresso.pressBack()
    }

    /**
     * Test if the Exercises button navigates properly
     */
    @Test
    fun exerciseButtonTest() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.customExercisesButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_customExerciseActivity))
            .check(matches(isDisplayed()))
        Espresso.pressBack()
    }

    /**
     * Test if the Logout button navigates properly
     */
    @Test
    fun logoutButtonTest() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.logoutButton))
            .perform(click())
        Espresso.onView(withId(R.id.layout_loginActivity))
            .check(matches(isDisplayed()))
    }
}