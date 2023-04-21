/*
 *  Created by Team Symphony on 4/20/23, 2:11 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/20/23, 2:08 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.NotificationAdapter
import com.symphony.mrfit.data.model.Notification
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityCalendarBinding
import com.symphony.mrfit.ui.Helper.EXTRA_DATE
import com.symphony.mrfit.ui.Helper.toCalendar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen featuring a Material UI-based Calendar widget for easy date selection
 */

class CalendarActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendar: CalendarView
    private val launchNotificationBuilder =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val cal = Calendar.getInstance()
                cal.timeInMillis = result.data!!.getLongExtra(EXTRA_DATE, 0)
                calendar.setDate(cal)
                profileViewModel.getNotifications()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        calendar = binding.calendarView
        val alarmList = binding.calendarNotificationsRecyclerView
        val newAlarm = binding.addAlertButton
        val notifDays = ArrayList<Notification>()
        val sameDay = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        fun deleteNotification(date: String) {
            profileViewModel.deleteNotification(date)
            profileViewModel.getNotifications()
        }

        /**
         * Cancel a pending notification
         * PendingIntent must match the notificaiton to be cancelled
         */
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


        layoutManager = LinearLayoutManager(this)
        alarmList.layoutManager = layoutManager

        // Populate the calendar with current notifications
        profileViewModel.getNotifications()
        profileViewModel.notifications.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val notifications = it ?: return@Observer
            val alertDays = ArrayList<EventDay>()
            val todayAlerts = ArrayList<Notification>()
            notifDays.clear()

            // Iterate over all notifications, note any on the currently selected date
            for (n in notifications) {
                notifDays.add(n)
                val notificationDate = n.date!!.toDate()
                val selectedDate = calendar.firstSelectedDate.time
                if (sameDay.format(notificationDate).equals(sameDay.format(selectedDate))) {
                    todayAlerts.add(n)
                }
                alertDays.add(
                    EventDay(
                        toCalendar(n.date.toDate()),
                        R.drawable.alert_icon
                    )
                )
            }
            alarmList.adapter =
                NotificationAdapter(this, todayAlerts, ::deleteNotification, ::cancelNotification)

            calendar.setEvents(alertDays)
        })

        // When a new day is selected, check for notifications on that date
        calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDay = eventDay.calendar
                val todayAlerts = ArrayList<Notification>()
                for (n in notifDays) {
                    val notificationDate = n.date!!.toDate()
                    val clickedDate = clickedDay.time
                    if (sameDay.format(notificationDate).equals(sameDay.format(clickedDate))) {
                        todayAlerts.add(n)
                    }
                }
                alarmList.adapter = NotificationAdapter(
                    applicationContext,
                    todayAlerts,
                    ::deleteNotification,
                    ::cancelNotification
                )
            }
        })

        newAlarm.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra(EXTRA_DATE, calendar.firstSelectedDate.timeInMillis)
            launchNotificationBuilder.launch(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}