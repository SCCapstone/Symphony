/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:20 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.WorkoutAdapter
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutRoutineBinding
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY
import java.util.*

/**
 * View Class for display the Workouts belonging to the current Routine
 */

class WorkoutRoutineActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityWorkoutRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutRoutineBinding.inflate(layoutInflater)
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
        val routineName = binding.routineNameEditText
        val routineDesc = binding.workoutDescriptionEditText
        val workoutList = binding.workoutListView
        val startWorkout = binding.startWorkoutButton
        val newExercise = binding.newExerciseButton
        val saveWorkout = binding.saveWorkoutButton
        val deleteWorkout = binding.deleteWorkoutButton

        /**
         * Retrieve the extras passed to this intent
         * passedID = The ID of the parent Routine
         * passedName = The Name of the parent Routine
         * passedList = The workoutList from the parent Routine
         */
        val passedRoutineID = intent.extras!!.getString(EXTRA_IDENTITY)
        var passedList = ArrayList<String>()

        /**
         * Set the layout of the list of workouts presented to the user
         */
        layoutManager = LinearLayoutManager(this)
        workoutList.layoutManager = layoutManager

        /**
         * Populate the list with the workouts associated with this routine
         */
        exerciseViewModel.getRoutine(passedRoutineID!!)
        exerciseViewModel.routine.observe(this, Observer {
            val routine = it ?: return@Observer

            routineName.setText(routine.name)
            if (routine.workoutList != null) {
                passedList = routine.workoutList
                exerciseViewModel.getWorkouts(routine.workoutList)
            }
        })
        exerciseViewModel.workoutList.observe(this, Observer {
            val workList = it ?: return@Observer
            workoutList.adapter = WorkoutAdapter(this, workList, passedRoutineID, passedList)
        })

        /**
         * Start the current workout then save it to the User's history,
         * then return to their Home screen
         * TODO: Add a screen that keeps track of the Current workout,
         *  then move the history functionality to that screen's "Finish" button
         */
        startWorkout.setOnClickListener {
            profileViewModel.addWorkoutToHistory(History(routineName.text.toString(), Timestamp(Date())))
            exerciseViewModel.updateRoutine(routineName.text.toString(), passedRoutineID)
            Toast.makeText(
                applicationContext,
                "Your workout has been saved to your history",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        /**
         * Navigate to a screen to make a new workout
         */
        newExercise.setOnClickListener {
            val intent = Intent(this, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_ROUTINE, passedRoutineID)
            intent.putExtra(EXTRA_IDENTITY, NEW_ID)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_STRING,"New Workout")
            intent.putExtra(WorkoutTemplateActivity.EXTRA_REPS,"0")
            intent.putExtra(WorkoutTemplateActivity.EXTRA_LIST, passedList)
            startActivity(intent)
        }

        /**
         * Save the current Routine and go back to the user's Home screen
         */
        saveWorkout.setOnClickListener {
            exerciseViewModel.updateRoutine(routineName.text.toString(), passedRoutineID)
            finish()
        }

        /**
         * Remove the current workout from the user's list
         */
        deleteWorkout.setOnClickListener {
            exerciseViewModel.deleteRoutine(passedRoutineID)
            Toast.makeText(
                applicationContext,
                "This workout has been removed from your list",
                Toast.LENGTH_LONG
            ).show()
            finish()

        }
    }

    companion object {
        const val EXTRA_ROUTINE = "passed routine id"
        const val NEW_ID = "NEW"
    }

}