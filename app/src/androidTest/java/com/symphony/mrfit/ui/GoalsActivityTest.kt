package com.symphony.mrfit.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Rule
import org.junit.Test
import com.symphony.mrfit.R




class GoalsActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<GoalsActivity>()

    //TODO
    @Test
    fun checkActivityVisibility() {
    }
}

