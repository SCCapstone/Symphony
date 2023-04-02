/*
 *  Created by Team Symphony on 4/2/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 3:50 AM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Notification
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityCalendarBinding
import com.symphony.mrfit.ui.Helper.toCalendar
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]

        val calendar = binding.calendarView
        val alarmText = binding.notificationsForToday
        val notifDays = ArrayList<Notification>()

        profileViewModel.getNotifications()
        profileViewModel.notifications.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val notifications = it ?: return@Observer
            val alertDays = ArrayList<EventDay>()
            notifDays.clear()

            for (n in notifications) {
                notifDays.add(n)
                alertDays.add(
                    EventDay(
                        toCalendar(n.date!!.toDate()),
                        R.drawable.notification_icon
                    )
                )
            }

            calendar.setEvents(alertDays)
        })

        calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDay = eventDay.calendar
                val sameDay = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMMM dd, yy 'at' hh:mm a", Locale.getDefault())
                var myString = ""
                for (n in notifDays) {
                    if (sameDay.format(n.date!!.toDate()).equals(sameDay.format(clickedDay.time))) {
                        /**
                         * TODO: Throw results into a RecyclerView
                         */
                        myString += "${n.message} at ${outputFormat.format(n.date.toDate())}\n"
                    }
                }
                alarmText.text = myString
            }
        })
    }
}