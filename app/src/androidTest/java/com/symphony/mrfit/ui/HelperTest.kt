/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:50 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import java.util.Calendar
import java.util.Date


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

