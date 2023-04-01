/*
 *  Created by Team Symphony on 3/31/23, 11:31 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/31/23, 11:31 PM
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
import com.symphony.mrfit.data.exercise.NotificationAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityNotificationLogBinding

class NotificationLogActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityNotificationLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationLogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val notificationList = binding.notificationList
        val spinner = binding.loadingSpinner

        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        notificationList.layoutManager = layoutManager

        profileViewModel.notifications.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val notifications = it ?: return@Observer

            notificationList.adapter = NotificationAdapter(this, notifications)
            spinner.visibility = View.GONE
        })
    }
}