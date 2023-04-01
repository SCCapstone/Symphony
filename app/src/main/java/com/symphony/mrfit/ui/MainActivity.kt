/*
 *  Created by Team Symphony on 3/18/23, 1:06 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/18/23, 1:02 PM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R

/**
 * Landing screen, can add functionality here to grab information on User's device
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = Firebase.auth.currentUser
        if (user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_main)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {

            // Display a Toast whenever a movement is captured on the screen
            MotionEvent.ACTION_UP -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}