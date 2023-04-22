package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import com.symphony.mrfit.R
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class UserProfileActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<UserProfileActivity>()

    /*tests if you can logout from user profile*/
    @Test
    fun checkViewVisibility() {
        onView(withId(R.id.profileScreenView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.profileNameTextView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.weightLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.ageLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.heightLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}