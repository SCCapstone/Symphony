/*
 *  Created by Team Symphony on 4/2/23, 12:52 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 12:52 AM
 */

package com.symphony.mrfit.ui

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.WorkoutAdapter2
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityCurrentWorkoutBinding
import com.symphony.mrfit.ui.Helper.ZERO
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_LIST
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_STRING
import com.symphony.mrfit.ui.WorkoutRoutineActivity.Companion.EXTRA_ROUTINE
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for the user to keep track of their current Workout Routine.
 * They should be able to keep track of how long this Routine has been going,
 * as well as which Workouts they have completed.
 */

class CurrentWorkoutActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityCurrentWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCurrentWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

    }

    override fun onStart() {
        super.onStart()


        Log.d(ContentValues.TAG, "Activity has (Re)Started")

        // Bind variables to View elements
        val routineName = binding.currentRoutineName
        val workoutList = binding.currentWorkoutRecycler
        val timer = binding.workoutTimer
        val finishButton = binding.finishWorkoutButton

        /**
         * Retrieve the extras passed to this intent
         * passedList = The workoutList from the parent Routine
         */
        val passedRoutineName = intent.getStringExtra(EXTRA_STRING)
        val passedRoutineID = intent.getStringExtra(EXTRA_ROUTINE)
        val passedList = intent.getStringArrayListExtra(EXTRA_LIST)


        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        workoutList.layoutManager = layoutManager

        routineName.text = passedRoutineName
        exerciseViewModel.getWorkouts(passedList!!)
        exerciseViewModel.workoutList.observe(this, Observer {
            val workList = it ?: return@Observer

            workoutList.adapter = WorkoutAdapter2(this, workList)
        })


        // Start the timer and end it when the user is done with their workout
        timer.base = SystemClock.elapsedRealtime()
        timer.start()

        finishButton.setOnClickListener {
            timer.stop()
            val timeSpent = SystemClock.elapsedRealtime() - timer.base
            profileViewModel.addWorkoutToHistory(
                History(
                    passedRoutineName!!,
                    Timestamp(Date()),
                    timeSpent,
                    passedRoutineID
                )
            )
            Toast.makeText(
                applicationContext,
                "Your workout has been saved to your history",
                Toast.LENGTH_SHORT
            ).show()


            val dialog = Dialog(this)
            dialog.setContentView(R.layout.activity_post_workout)
            dialog.setTitle("Post Workout")

            val goals = dialog.findViewById<Button>(R.id.gotoGoalsButton)
            val home = dialog.findViewById<Button>(R.id.returnHomeButton)
            val text = dialog.findViewById<TextView>(R.id.postWorkoutTime)
            val startTime = SimpleDateFormat(
                "'You started this workout at' hh:mm a",
                Locale.getDefault()
            )
                .format(Date().time - timeSpent)
            val endTime = SimpleDateFormat(
                "' and finished at ' hh:mm a",
                Locale.getDefault()
            )
                .format(Date())
            var totalTime = " for a total of "
            var hours: Long = 0
            var minutes = timeSpent / 1000 / 60
            val seconds = timeSpent / 1000 % 60
            if (minutes >= 60) {
                hours = minutes % 60
                minutes /= 60
            }

            if (hours > ZERO) {
                totalTime += "$hours hours and $minutes minutes "
            } else if (minutes > ZERO) {
                totalTime += "$minutes minutes and "
            }
            totalTime += "$seconds seconds."

            text.text = startTime + endTime + totalTime

            dialog.setOnCancelListener {
                gotoHome()
            }

            home.setOnClickListener {
                gotoHome()
            }

            goals.setOnClickListener {
                gotoGoals()
            }

            dialog.show()
        }
    }

    private fun gotoHome() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun gotoGoals() {
        val homeIntent = Intent(applicationContext, HomeActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(homeIntent)

        val userIntent = Intent(applicationContext, UserProfileActivity::class.java)
        userIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(userIntent)

        val goalIntent = Intent(applicationContext, GoalsActivity::class.java)
        startActivity(goalIntent)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}