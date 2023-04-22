package com.symphony.mrfit.ui

import android.app.Activity
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*


class HelperTest{
    private lateinit var activity: Activity

    //test functionality of calendar method
    @Test
    fun toCalendarconvertsDatetoCalendar() {
        val date = Date()
        val cal = Helper.toCalendar(date)
        assertThat(cal.get(Calendar.YEAR), `is`(Calendar.getInstance().get(Calendar.YEAR)))
        assertThat(cal.get(Calendar.MONTH), `is`(Calendar.getInstance().get(Calendar.MONTH)))
        assertThat(cal.get(Calendar.DAY_OF_MONTH), `is`(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        )
    }
}