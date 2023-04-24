/*
 *  Created by Team Symphony on 4/24/23, 2:09 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 2:09 AM
 */

package com.symphony.mrfit.ui

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Calendar
import java.util.Date
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

        @Test
        fun `calendar test`() {
            val expected = "01:01 AM"
            assertEquals(expected, Helper.humanReadableTime(calendar))
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

        @Test
        fun `calendar test`() {
            val expected = "February 01, 1999"
            assertEquals(expected, Helper.humanReadableDate(calendar))
        }
    }

    @Nested
    inner class HumanReadableShortDate {
        @Test
        fun `long test`() {
            val expected = "02-01-1999"
            assertEquals(expected, Helper.humanReadableShortDate(longTime))
        }

        @Test
        fun `date test`() {
            val expected = "02-01-1999"
            assertEquals(expected, Helper.humanReadableShortDate(date))
        }

        @Test
        fun `calendar test`() {
            val expected = "02-01-1999"
            assertEquals(expected, Helper.humanReadableShortDate(calendar))
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

        @Test
        fun `calendar test`() {
            val expected = "February 01, 1999 at 01:01 AM"
            assertEquals(expected, Helper.humanReadableDateTime(calendar))
        }
    }

    @Nested
    inner class SillyTests {
        @Test
        fun `equal add`() {
            val expected = 5
            assertEquals(expected, Helper.myAdd(2, 3))
        }

        @Test
        fun `not equal add`() {
            val unexpected = -5
            assertNotEquals(unexpected, Helper.myAdd(2, 3))
        }

        @Test
        fun `equal sub`() {
            val expected = -1
            assertEquals(expected, Helper.mySub(2, 3))
        }

        @Test
        fun `not equal sub`() {
            val unexpected = 1
            assertNotEquals(unexpected, Helper.mySub(2, 3))
        }

        @Test
        fun `equal multi`() {
            val expected = 6
            assertEquals(expected, Helper.myMulti(2, 3))
        }

        @Test
        fun `not equal multi`() {
            val unexpected = -6
            assertNotEquals(unexpected, Helper.myMulti(2, 3))
        }

        @Test
        fun `equal div`() {
            val expected = 0
            assertEquals(expected, Helper.myDiv(2, 3))
        }

        @Test
        fun `not equal div`() {
            val unexpected = 1
            assertNotEquals(unexpected, Helper.myDiv(2, 3))
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