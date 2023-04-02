/*
 *  Created by Team Symphony on 4/1/23, 10:04 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 10:04 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.HistoryAdapter2
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutHistoryBinding

class WorkoutHistoryActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var binding: ActivityWorkoutHistoryBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val historyList = binding.historyList
        val spinner = binding.loadingSpinner

        fun deleteHistory(historyID: String) {
            profileViewModel.deleteWorkoutFromHistory(historyID)
        }

        fun openHistory(history: History) {

        }

        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        historyList.layoutManager = layoutManager

        profileViewModel.getWorkoutHistory()
        profileViewModel.workoutHistory.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val workoutHistory = it ?: return@Observer

            historyList.adapter =
                HistoryAdapter2(this, workoutHistory, ::deleteHistory, ::openHistory)
            spinner.visibility = View.GONE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}