/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.ui
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CurrentWorkoutActivityTest {
    //behavioral
    @Test
    fun onCreate() {

    }
    //behavioral
    @Test
    fun onStart() {

    }
    @Test
    fun gotoHome() {
        // Start the activity
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val expectedFlags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK)

        assertEquals(expectedFlags, intent.flags)
        assertEquals(context.packageName, intent.component?.packageName)
        assertEquals(HomeActivity::class.java.name, intent.component?.className)
    }
    //behavioral
    @Test
    fun onSupportNavigateUp() {

    }
}