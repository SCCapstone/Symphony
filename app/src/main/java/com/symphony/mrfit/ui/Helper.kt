/*
 *  Created by Team Symphony on 4/24/23, 2:09 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 2:09 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Object for holding helper functions and consts
 * TODO: Move all of the EXTRA tags here
 */

object Helper {
    const val BLANK = ""
    const val ZERO = 0
    const val EXTRA_DATE = "passed_date"
    const val ONE_MINUTE: Long = 60000
    const val ONE_HOUR: Long = 3600000

    /**
     * Simplified snackbar generator
     */
    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Convert a Date object to a Calendar
     */
    fun toCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    /**
     * Convert a time in milliseconds to the format of HH:MM:SS
     * If the time is less than an hour, that field should be omitted
     */
    fun humanReadableDuration(time: Long): String {
        return if (time >= ONE_HOUR) {
            String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            )
        } else {
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            )
        }
    }

    /**
     * Get the time in a 12-hour format from a Long
     */
    fun humanReadableTime(time: Long): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
            .format(time)
    }

    /**
     * Get the time in a 12-hour format from a Date
     */
    fun humanReadableTime(time: Date): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
            .format(time)
    }


    /**
     * Get the time in a 12-hour format from a Calendar
     */
    fun humanReadableTime(time: Calendar): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
            .format(time.time)
    }


    /**
     * Get the time in a 'Month 00, 0000' format from a Long
     */
    fun humanReadableDate(date: Long): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
            .format(date)
    }

    /**
     * Get the time in a 'Month 00, 0000' format from a Date
     */
    fun humanReadableDate(date: Date): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
            .format(date)
    }

    /**
     * Get the time in a 'Month 00, 0000' format from a Calendar
     */
    fun humanReadableDate(date: Calendar): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
            .format(date.time)
    }

    /**
     * Get the time in a 'MM-00-0000' format from a Long
     */
    fun humanReadableShortDate(date: Long): String {
        return SimpleDateFormat(
            "MM-dd-yyyy",
            Locale.US
        )
            .format(date)
    }

    /**
     * Get the time in a 'MM-00-0000' format from a Date
     */
    fun humanReadableShortDate(date: Date): String {
        return SimpleDateFormat(
            "MM-dd-yyyy",
            Locale.US
        )
            .format(date)
    }

    /**
     * Get the time in a 'MM-00-0000' format from a Calendar
     */
    fun humanReadableShortDate(date: Calendar): String {
        return SimpleDateFormat(
            "MM-dd-yyyy",
            Locale.US
        )
            .format(date.time)
    }

    fun humanReadableDateTime(date: Long): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm a",
            Locale.getDefault()
        )
            .format(date)
    }

    fun humanReadableDateTime(date: Date): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm a",
            Locale.getDefault()
        )
            .format(date)
    }

    fun humanReadableDateTime(date: Calendar): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm a",
            Locale.getDefault()
        )
            .format(date.time)
    }

    /**
     * Silly tests meant to act as Unit Testing fodder
     */
    fun myAdd(a: Int, b: Int): Int {
        return a + b
    }

    fun mySub(a: Int, b: Int): Int {
        return a - b
    }

    fun myMulti(a: Int, b: Int): Int {
        return a * b
    }

    fun myDiv(a: Int, b: Int): Int {
        return a / b
    }
}