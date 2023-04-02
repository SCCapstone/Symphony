/*
 *  Created by Team Symphony on 4/2/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 2:45 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Object for holding helper functions and consts
 * TODO: Move all of the EXTRA tags here
 */

object Helper {
    const val BLANK = ""
    const val ZERO = 0
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
}