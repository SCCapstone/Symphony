/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 4:24 PM
 */

package com.symphony.mrfit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.databinding.ActivityDebugBinding
import com.symphony.mrfit.ui.*

/**
 * Menu for instantly navigating to any page for debug and testing
 * To add a new link, make a button in the layout, connect to the binding
 * and attach a setOnClickListener. Copy/Paste existing examples if needed.
 */

class DebugActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = binding.debugLogin
        val register = binding.debugRegister
        val profile = binding.debugUserProfile
        val calendar = binding.debugCalendar
        val notification = binding.debugNotification
        val workout = binding.debugWorkout

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        calendar.setOnClickListener {
            val intent = Intent(this, Calendar::class.java)
            startActivity(intent)
        }

        notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
        workout.setOnClickListener {
            val intent = Intent(this, WorkoutRoutineActivity::class.java)
            startActivity(intent)
        }
    }
}