/*
 *  Created by Team Symphony on 4/20/23, 7:03 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/20/23, 7:03 PM
 */

package com.symphony.mrfit

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.RoutineAdapter
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityManualWorkoutBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for the User to manually add a workout History
 */

class ManualWorkoutActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityManualWorkoutBinding
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()

        val routineList = binding.templateRecyclerList
        val startTimePicker = binding.startTimeRow
        val endTimePicker = binding.endTimeRow
        val startTimeSelection = binding.startTimeSelection
        val endTimeSelection = binding.endTimeSelection
        val save = binding.saveManualWorkoutButton
        val dateFormat = SimpleDateFormat(
            "MMMM dd, yyyy 'at' hh:mm a",
            Locale.getDefault()
        )

        // Set the layout of the grid of routines presented to the user
        layoutManager = GridLayoutManager(this, 2)
        routineList.layoutManager = layoutManager

        // Initialize the current date time, will be overwritten later if needed
        startTimeSelection.text = dateFormat.format(Date())
        endTimeSelection.text = dateFormat.format(Date())
        startTime = Date().time
        endTime = Date().time + 1

        // Initialize the routine list, then listen to it to update the UI
        exerciseViewModel.getUserRoutines()
        exerciseViewModel.workoutRoutineList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating routine list")
            val workoutRoutineList = it ?: return@Observer

            routineList.adapter = RoutineAdapter(this, workoutRoutineList)

        })

        /**
         * Open a dialog for the user to select a start time
         * If selected time is after end time, adjust end time accordingly
         */
        startTimePicker.setOnClickListener {
            // Create the dialog and inflate its view like an activity
            val materialDialog = MaterialAlertDialogBuilder(this)
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.popup_date_and_time_picker, null, false)

            materialDialog.setView(dialogView)

            val datePicker =
                findViewById<com.applandeo.materialcalendarview.CalendarView>(R.id.dialogDatePicker)
            val timePicker = findViewById<TimePicker>(R.id.dialogTimePicker)
            val cal = Calendar.getInstance()
            cal.timeInMillis = endTime
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)
            var hour = cal.get(Calendar.HOUR)
            var minute = cal.get(Calendar.MINUTE)

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

            materialDialog.setPositiveButton(getString(R.string.button_ok)) { dialog, _ ->
                // Check if the end time needs to be adjusted
                val newStart = Calendar.getInstance()
                newStart.set(year, month, day, hour, minute)
                val t = Calendar.getInstance()
                t.timeInMillis = endTime
                if (newStart.after(t)) {
                    // Times are off, set the start time to 1 minute after the new start time
                    t.set(year, month, day, hour, minute + 1)
                }

                // Set the text views
                startTimeSelection.text = dateFormat.format(newStart.time)
                endTimeSelection.text = dateFormat.format(t.time)

                dialog.dismiss()
            }

            materialDialog.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            materialDialog.show()
        }

        /**
         * Open a dialog for the user to select an end time
         * If selected time is before start time, adjust start time accordingly
         */
        endTimePicker.setOnClickListener {
            // Create the dialog and inflate its view like an activity
            val materialDialog = MaterialAlertDialogBuilder(this)
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.popup_date_and_time_picker, null, false)

            materialDialog.setView(dialogView)

            val datePicker =
                findViewById<com.applandeo.materialcalendarview.CalendarView>(R.id.dialogDatePicker)
            val timePicker = findViewById<TimePicker>(R.id.dialogTimePicker)
            val cal = Calendar.getInstance()
            cal.timeInMillis = endTime
            datePicker.setDate(cal)
            var year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            var day = cal.get(Calendar.DAY_OF_MONTH)
            var hour = cal.get(Calendar.HOUR)
            var minute = cal.get(Calendar.MINUTE)

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

            materialDialog.setPositiveButton(getString(R.string.button_ok)) { dialog, _ ->
                // Check if the start time needs to be adjusted
                val newEnd = Calendar.getInstance()
                newEnd.set(year, month, day, hour, minute)
                val t = Calendar.getInstance()
                t.timeInMillis = startTime
                if (newEnd.before(t)) {
                    // Times are off, set the start time to 1 minute before the new end time
                    t.set(year, month, day, hour, minute - 1)
                }

                // Set the text views
                endTimeSelection.text = dateFormat.format(newEnd.time)
                startTimeSelection.text = dateFormat.format(t.time)

                dialog.dismiss()
            }

            materialDialog.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            materialDialog.show()
        }

        /**
         * Create a history from the given information and add it to the database
         * A template must be selected
         * The end time must not be before the start time
         */
        save.setOnClickListener {
            val selection = true
            if (selection) {
                if (startTime < endTime) {
                    profileViewModel.addWorkoutToHistory(
                        History(
                            "REPLACE ME",
                            Timestamp(Date(startTime)),
                            endTime - startTime,
                            "REPLACE ME"
                        )
                    )
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Times are out of order",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Must select a template",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}