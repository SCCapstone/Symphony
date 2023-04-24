/*
 *  Created by Team Symphony on 4/24/23, 2:09 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 1:49 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.symphony.mrfit.data.adapters.RoutineAdapter2
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
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
    private lateinit var startTimeSelection: TextView
    private lateinit var endTimeSelection: TextView
    private var startTime: Long = 0
    private var endTime: Long = 0
    private val dateFormat = SimpleDateFormat(
        "MMMM dd, yyyy 'at' hh:mm a",
        Locale.getDefault()
    )

    private val launchStartTimePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                // User picked a time, sanity check it is before the current end time
                val newStart = Calendar.getInstance()
                newStart.timeInMillis = result.data!!.getLongExtra(EXTRA_TIME, 0)
                val t = Calendar.getInstance()
                t.timeInMillis = endTime
                if (newStart.after(t)) {
                    // Times are off, set the end time to at least 1 minute after the new start time
                    val diff = intent.getLongExtra(TIME_DIFF, ONE_MINUTE.toLong())
                    t.timeInMillis = newStart.timeInMillis + diff
                }
                startTime = newStart.timeInMillis
                endTime = t.timeInMillis

                // Set the text views
                setText()
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                /* Intentionally left blank */
            }
        }

    private val launchEndTimePicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                // User picked a time, sanity check it is before the current end time
                val newEnd = Calendar.getInstance()
                newEnd.timeInMillis = result.data!!.getLongExtra(EXTRA_TIME, 0)
                val t = Calendar.getInstance()
                t.timeInMillis = startTime
                if (newEnd.before(t)) {
                    // Times are off, set the start time to at least 1 minute before the new end time
                    val diff = intent.getLongExtra(TIME_DIFF, ONE_MINUTE.toLong())
                    t.timeInMillis = newEnd.timeInMillis - diff
                }
                endTime = newEnd.timeInMillis
                startTime = t.timeInMillis

                // Set the text views
                setText()
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                /* Intentionally left blank */
            }
        }

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
        startTimeSelection = binding.startTimeSelection
        endTimeSelection = binding.endTimeSelection
        val save = binding.saveManualWorkoutButton

        var selectedThing = intent.getIntExtra(SAVED_SELECTION, -999)
        var routineName = intent.getStringExtra(SAVED_NAME)
        var routineID = intent.getStringExtra(SAVED_ID)

        fun update(name: String, id: String, num: Int) {
            routineName = name
            routineID = id
            selectedThing = num
        }

        // Set the layout of the grid of routines presented to the user
        layoutManager = GridLayoutManager(this, 2)
        routineList.layoutManager = layoutManager

        // Initialize the current date time, will be overwritten later if needed
        startTime = intent.getLongExtra(SAVED_START, Date().time)
        endTime = intent.getLongExtra(SAVED_END, Date().time + ONE_MINUTE)
        setText()

        // Initialize the routine list, then listen to it to update the UI
        exerciseViewModel.getUserRoutines()
        exerciseViewModel.workoutRoutineList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating routine list")
            val workoutRoutineList = it ?: return@Observer

            routineList.adapter = RoutineAdapter2(this, workoutRoutineList, ::update, selectedThing)

        })

        /**
         * Navigate to a screen for the user to select a start time, await its result
         * If selected time is after end time, adjust end time accordingly
         */
        startTimePicker.setOnClickListener {
            intent.putExtra(SAVED_START, startTime)
            intent.putExtra(SAVED_END, endTime)
            intent.putExtra(SAVED_SELECTION, selectedThing)
            intent.putExtra(SAVED_NAME, routineName)
            intent.putExtra(SAVED_ID, routineID)
            intent.putExtra(TIME_DIFF, endTime - startTime)
            val picker = Intent(this, DateTimeActivity::class.java)
            picker.putExtra(EXTRA_TIME, startTime)
            launchStartTimePicker.launch(picker)
        }

        /**
         * Navigate to a screen for the user to select an end time, await its result
         * If selected time is before start time, adjust start time accordingly
         */
        endTimePicker.setOnClickListener {
            intent.putExtra(SAVED_START, startTime)
            intent.putExtra(SAVED_END, endTime)
            intent.putExtra(SAVED_SELECTION, selectedThing)
            intent.putExtra(SAVED_NAME, routineName)
            intent.putExtra(SAVED_ID, routineID)
            intent.putExtra(TIME_DIFF, endTime - startTime)
            val picker = Intent(this, DateTimeActivity::class.java)
            picker.putExtra(EXTRA_TIME, endTime)
            launchEndTimePicker.launch(picker)
        }

        /**
         * Create a history from the given information and add it to the database
         * A template must be selected
         * The end time must not be before the start time
         */
        save.setOnClickListener {
            if (selectedThing >= 0) {
                if (startTime < endTime) {
                    binding.loadingSpinner.visibility = View.VISIBLE
                    profileViewModel.addWorkoutToHistory(
                        History(
                            routineName!!,
                            Timestamp(Date(startTime)),
                            endTime - startTime,
                            routineID
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

    private fun setText() {
        startTimeSelection.text = dateFormat.format(startTime)
        endTimeSelection.text = dateFormat.format(endTime)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_TIME = "passed time"
        const val SAVED_START = "saved start time"
        const val SAVED_END = "saved end time"
        const val SAVED_SELECTION = "saved selection"
        const val SAVED_NAME = "saved name"
        const val SAVED_ID = "saved id"
        const val TIME_DIFF = "difference between times"
        const val ONE_MINUTE = 60000
    }
}