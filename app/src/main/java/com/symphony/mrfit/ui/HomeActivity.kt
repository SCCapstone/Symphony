/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.HistoryAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory())[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val screen = binding.homeScreenView
        val spinner = binding.loadingSpinner
        val userProfile = binding.userLayout
        val name = binding.homeNameTextView
        val scheduleWorkout = binding.scheduleButton
        val startWorkout = binding.workoutButton
        val historyList = binding.historyList

        // Hide the screen till loading is done
        screen.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.fetchCurrentUser()
        profileViewModel.getWorkoutHistory()


        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        historyList.layoutManager = layoutManager

        profileViewModel.loggedInUser.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val loggedInUser = it ?: return@Observer

            name.text = loggedInUser.name
            screen.visibility = View.VISIBLE
        })

        profileViewModel.workoutHistory.observe(this, Observer {
            Log.d(ContentValues.TAG, "Reading user's workout history")
            val workoutHistory = it ?: return@Observer

            historyList.adapter = HistoryAdapter(this, workoutHistory)
            spinner.visibility = View.GONE
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
            val intent = Intent(this, RoutineSelectionActivity::class.java)
            startActivity(intent)
        }
    }
}