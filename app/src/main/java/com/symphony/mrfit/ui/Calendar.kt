/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.R

class Calendar : AppCompatActivity() {

    // variables for text view and calendar view
    private lateinit var date: TextView
    lateinit var calendarView: CalendarView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        date = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)

        calendarView
            .setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                val Date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)

                date.text = Date
            }

    }
}