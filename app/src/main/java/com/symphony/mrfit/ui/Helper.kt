package com.symphony.mrfit.ui

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

object Helper {
    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}