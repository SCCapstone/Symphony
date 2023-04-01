/*
 *  Created by Team Symphony on 4/1/23, 5:08 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 5:08 AM
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
import com.symphony.mrfit.data.exercise.GoalAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityGoalsBinding

class GoalsActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val goalsList = binding.goalsList
        val spinner = binding.loadingSpinner

        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        goalsList.layoutManager = layoutManager

        profileViewModel.goals.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val goals = it ?: return@Observer

            goalsList.adapter = GoalAdapter(this, goals)
            spinner.visibility = View.GONE
        })
    }
}