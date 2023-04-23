/*
 *  Created by Team Symphony on 4/23/23, 3:02 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/23/23, 3:02 AM
 */

package com.symphony.mrfit.ui

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.properties.Delegates

internal class HelperTest {

    @BeforeEach
    fun setUp() {
        oneSecond = 1000
        oneMinute = 60000
        oneHour = 3600000
        calendar = Calendar.getInstance()
        calendar.set(1999, 1, 1, 1, 1, 1)
        date = calendar.time
        longTime = date.time
    }

    @Test
    fun toCalendarTest() {
        val cal = Helper.toCalendar(Date())
        MatcherAssert.assertThat(
            cal.get(Calendar.YEAR),
            CoreMatchers.`is`(Calendar.getInstance().get(Calendar.YEAR))
        )
        MatcherAssert.assertThat(
            cal.get(Calendar.MONTH),
            CoreMatchers.`is`(Calendar.getInstance().get(Calendar.MONTH))
        )
        MatcherAssert.assertThat(
            cal.get(Calendar.DAY_OF_MONTH),
            CoreMatchers.`is`(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        )
    }

    @Nested
    inner class HumanReadableDuration {
        @Test
        fun `one second test`() {
            val expected = "00:01"
            assertEquals(expected, Helper.humanReadableDuration(oneSecond))
        }

        @Test
        fun `one minute test`() {
            val expected = "01:00"
            assertEquals(expected, Helper.humanReadableDuration(oneMinute))
        }

        @Test
        fun `one hour test`() {
            val expected = "01:00:00"
            assertEquals(expected, Helper.humanReadableDuration(oneHour))
        }

    }

    @Nested
    inner class HumanReadableTime {
        @Test
        fun `long test`() {
            val expected = "01:01 AM"
            assertEquals(expected, Helper.humanReadableTime(longTime))
        }

        @Test
        fun `date test`() {
            val expected = "01:01 AM"
            assertEquals(expected, Helper.humanReadableTime(date))
        }
    }

    @Nested
    inner class HumanReadableDate {
        @Test
        fun `long test`() {
            val expected = "February 01, 1999"
            assertEquals(expected, Helper.humanReadableDate(longTime))
        }

        @Test
        fun `date test`() {
            val expected = "February 01, 1999"
            assertEquals(expected, Helper.humanReadableDate(date))
        }
    }

    @Nested
    inner class HumanReadableDateTime {
        @Test
        fun `long test`() {
            val expected = "February 01, 1999 at 01:01 AM"
            assertEquals(expected, Helper.humanReadableDateTime(longTime))
        }

        @Test
        fun `date test`() {
            val expected = "February 01, 1999 at 01:01 AM"
            assertEquals(expected, Helper.humanReadableDateTime(date))
        }
    }

    companion object {
        var oneSecond by Delegates.notNull<Long>()
        var oneMinute by Delegates.notNull<Long>()
        var oneHour by Delegates.notNull<Long>()
        var longTime by Delegates.notNull<Long>()
        lateinit var calendar: Calendar
        lateinit var date: Date
    }
}