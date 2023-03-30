/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.R

/**
 * Is this going to be the Home screen or the Welcome screen?
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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