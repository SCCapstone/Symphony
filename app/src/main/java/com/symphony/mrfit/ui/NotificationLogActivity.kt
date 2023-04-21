/*
 *  Created by Team Symphony on 4/2/23, 9:44 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 9:42 PM
 */

package com.symphony.mrfit.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
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
        val newNotif = binding.addNotificationButton
        val spinner = binding.loadingSpinner

        fun deleteNotification(date: String) {
            profileViewModel.deleteNotification(date)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun cancelNotification(time: String) {
            val notificationIntent = Intent(applicationContext, Notifications::class.java)
            notificationIntent.data = Uri.parse(time)
            Log.e("Notifications", "Cancelling alarm: $time")

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                time.toLong().toInt(),
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        notificationList.layoutManager = layoutManager

        profileViewModel.notifications.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val notifications = it ?: return@Observer

            notifications.reverse()
            notificationList.adapter =
                NotificationAdapter(this, notifications, ::deleteNotification, ::cancelNotification)
            spinner.visibility = View.GONE
        })

        newNotif.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}