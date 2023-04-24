/*
 *  Created by Team Symphony on 4/24/23, 2:09 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 1:45 AM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.auth.FirebaseAuth
import com.symphony.mrfit.R
import com.symphony.mrfit.data.adapters.HistoryAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityHomeBinding
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        // Return to the login screen if the User is somehow not logged in
        firebaseAuth.addAuthStateListener(this.authStateListener)

        val screen = binding.homeScreenView
        val spinner = binding.loadingSpinner
        val userProfile = binding.userLayout
        val name = binding.homeNameTextView
        val pfp = binding.homeProfilePicture
        val scheduleWorkout = binding.scheduleButton
        val manualWorkout = binding.pastWorkout
        val startWorkout = binding.workoutButton
        val historyList = binding.historyList

        screen.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.fetchCurrentUser()
        profileViewModel.getWorkoutHistory()


        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        historyList.layoutManager = layoutManager

        profileViewModel.loggedInUser.observe(this, Observer {
            val loggedInUser = it ?: return@Observer

            if (loggedInUser.userID == "delete") {
                Toast.makeText(
                    this,
                    "We're sorry! There was an error in your account and it had to be reset. Please log back in.",
                    Toast.LENGTH_LONG
                ).show()
            }

            Glide.with(this)
                .load(profileViewModel.getProfilePicture())
                .placeholder(R.drawable.placeholder_profile_picture)
                .circleCrop()
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(pfp)
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
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        manualWorkout.setOnClickListener {
            val intent = Intent(this, ManualWorkoutActivity::class.java)
            startActivity(intent)
        }

        startWorkout.setOnClickListener {
            val intent = Intent(this, RoutineSelectionActivity::class.java)
            startActivity(intent)
        }

        historyList.setOnClickListener {
            val intent = Intent(this, WorkoutHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this.authStateListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}