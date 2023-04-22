/*
 *  Created by Team Symphony on 4/21/23, 10:18 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/21/23, 8:55 PM
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
class UserProfileActivityTest {

    @Before
    fun setUp() {
    }

    @get:Rule
    var activityScenarioRule = activityScenarioRule<UserProfileActivity>()

    /**
     * Test if the activity is displayed and visible to user
     */
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_profileActivity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Test if all the appropriate components are visible
     */
    @Test
    fun checkViewVisibility() {
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.profileScreenView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.profilePicture))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.profileNameTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.editProfileButton))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.heightLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.weightLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.ageLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.stuffLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.goalsButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.notificationsButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.historyButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.customExercisesButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.settingLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.notificationToggle))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(ViewMatchers.withId(R.id.logoutButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.deleteButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.loadingSpinner))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }
}