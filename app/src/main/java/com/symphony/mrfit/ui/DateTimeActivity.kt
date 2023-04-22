/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 5:12 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.symphony.mrfit.databinding.ActivityDateTimeBinding
import com.symphony.mrfit.ui.ManualWorkoutActivity.Companion.EXTRA_TIME
import java.util.*

class DateTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDateTimeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datePicker = binding.dialogDatePicker
        val timePicker = binding.dialogTimePicker
        val okay = binding.dateTimeOK
        val cancel = binding.dateTimeCancel

        val cal = Calendar.getInstance()
        cal.timeInMillis = intent.getLongExtra(EXTRA_TIME, Date().time)
        var year = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DAY_OF_MONTH)
        var hour = cal.get(Calendar.HOUR)
        var minute = cal.get(Calendar.MINUTE)

        datePicker.setDate(cal)
        timePicker.hour = hour
        timePicker.minute = minute

        timePicker.setOnTimeChangedListener { _, selectedHour, selectedMinute ->
            hour = selectedHour
            minute = selectedMinute
        }

        datePicker.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDay = eventDay.calendar
                year = clickedDay.get(Calendar.YEAR)
                month = clickedDay.get(Calendar.MONTH)
                day = clickedDay.get(Calendar.DAY_OF_MONTH)
            }
        })

        okay.setOnClickListener {
            val t = Calendar.getInstance()
            t.set(year, month, day, hour, minute)
            val intent = Intent()
            intent.putExtra(EXTRA_TIME, t.timeInMillis)
            this.setResult(Activity.RESULT_OK, intent)
            this.finish()
        }

        cancel.setOnClickListener {
            this.setResult(Activity.RESULT_CANCELED)
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}