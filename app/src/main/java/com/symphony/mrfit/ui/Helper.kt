/*
 *  Created by Team Symphony on 2/25/23, 1:08 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/25/23, 12:33 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

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
}