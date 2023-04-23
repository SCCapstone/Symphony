/*
 *  Created by Team Symphony on 4/23/23, 3:02 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 11:42 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
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

    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    fun toCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

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

    fun humanReadableTime(time: Long): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
            .format(time)
    }

    fun humanReadableTime(time: Date): String {
        return SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
            .format(time)
    }

    fun humanReadableDate(date: Date): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
            .format(date)
    }

    fun humanReadableDate(date: Long): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy",
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

    fun humanReadableDateTime(date: Long): String {
        return SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm a",
            Locale.getDefault()
        )
            .format(date)
    }
}