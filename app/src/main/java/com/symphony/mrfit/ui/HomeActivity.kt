/*
 * Created by Team Symphony 11/26/22, 3:06 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/26/22, 3:06 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory())[ProfileViewModel::class.java]

        val userProfile = binding.userLayout
        val name = binding.homeNameTextView
        val scheduleWorkout = binding.scheduleButton
        val startWorkout = binding.workoutButton

        /**
         * Get data of current User and populate the page
         */
        profileViewModel.fetchCurrentUser()
        profileViewModel.loggedInUser.observe(this, Observer {
            Log.d(ContentValues.TAG, "Populating Profile screen with values from current user")
            val loggedInUser = it ?: return@Observer

            name.text = loggedInUser.name
        })

        userProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        scheduleWorkout.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        startWorkout.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "here",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}