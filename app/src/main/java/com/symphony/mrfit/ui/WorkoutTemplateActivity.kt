/*
 *  Created by Team Symphony on 4/22/23, 3:13 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 2:45 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseRepository
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.databinding.ActivityWorkoutTemplateBinding
import com.symphony.mrfit.ui.Helper.showSnackBar
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.NEW_ID
import com.symphony.mrfit.ui.WorkoutRoutineActivity.Companion.EXTRA_ROUTINE

/**
 * Screen for modifying a Workout
 */

class WorkoutTemplateActivity : AppCompatActivity() {
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityWorkoutTemplateBinding
    private lateinit var status: String
    private val launchExerciseSelection =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                // User pick an exercise, so get its data
                exerciseViewModel.getExercise(result.data?.getStringExtra(EXTRA_IDENTITY)!!)
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // User did not pick an exercise
                // If this was supposed to be a new exercise, finish activity to return
                if (status == getString(R.string.picking_exercise))
                    finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

            val home = binding.BackButton
            home.setOnClickListener {
                val intent = Intent(this, Workout::class.java)
                startActivity(intent)

            }
    }

    override fun onStart() {
        super.onStart()

        val storage: FirebaseStorage = Firebase.storage

        val duration = binding.editDuration
        val distance = binding.editDistance
        val reps = binding.editReps
        val sets = binding.editSets
        val saveButton = binding.saveTemplateButton
        val deleteButton = binding.deleteTemplateButton
        val exeCard = binding.exerciseCardView
        var exeID = ""

        /**
         * Retrieve the extras passed to this intent
         * passedWorkoutID = The ID of the current workout
         * passedName = The Name of the Workout
         * passedRep = The NumberOfReps from the Workout
         * passedList = The workoutList from the parent Routine
         */
        val passedRoutineID = intent.getStringExtra(EXTRA_ROUTINE)
        val passedWorkoutID = intent.getStringExtra(EXTRA_IDENTITY)
        val passedList: ArrayList<String>? = intent.getStringArrayListExtra(EXTRA_LIST)

        // If this is a new exercise, skip past this screen
        status = intent.getStringExtra(EXTRA_STRING).toString()
        if (status == getString(R.string.new_exercise)) {
            intent.putExtra(EXTRA_STRING, getString(R.string.picking_exercise))
            gotoExerciseScreen()
        }

        // If passed an existing workout, populate the appropriate fields
        if (passedWorkoutID != NEW_ID) {
            val passedDuration = intent.getStringExtra(EXTRA_DURA)
            val passedDistance = intent.getStringExtra(EXTRA_DIST)
            val passedReps = intent.getIntExtra(EXTRA_REPS, INT_NULL)
            val passedSets = intent.getIntExtra(EXTRA_SETS, INT_NULL)
            deleteButton.visibility = View.VISIBLE

            if (passedDuration != null) {
                duration.setText(passedDuration)
            }
            if (passedDistance != null) {
                distance.setText(passedDistance)
            }
            if (passedReps != INT_NULL) {
                reps.setText(passedReps.toString())
            }
            if (passedSets != INT_NULL) {
                sets.setText(passedSets.toString())
            }

            exerciseViewModel.getExercise(intent.getStringExtra(EXTRA_EXERCISE)!!)
        }

        // When tapping the exercise card, go to exercise selection
        exeCard.root.setOnClickListener {
            gotoExerciseScreen()
        }


        // Save the workout and return to the parent Routine
        saveButton.setOnClickListener {
            val newWorkoutName = exeCard.exerciseNameTextView.text.toString()
            val newDuration: String? = if (duration.text!!.isNotEmpty()) {
                duration.text.toString()
            } else {
                null
            }
            val newDistance: String? = if (distance.text!!.isNotEmpty()) {
                distance.text.toString()
            } else {
                null
            }
            val newReps: Int? = if (reps.text!!.isNotEmpty()) {
                reps.text.toString().toInt()
            } else {
                null
            }
            val newSets: Int? = if (sets.text!!.isNotEmpty()) {
                sets.text.toString().toInt()
            } else {
                null
            }

            if (passedWorkoutID != NEW_ID) {
                /**
                 * TODO: If a field is left blank when updating a workout, preserve the old data
                 */
                // Update a workout in the database
                exerciseViewModel.updateWorkout(
                    Workout(
                        newWorkoutName,
                        newDuration,
                        newDistance,
                        newReps,
                        newSets,
                        exeID,
                        passedWorkoutID
                    )
                )
            } else {
                // Add a workout to the database
                val workoutID = exerciseViewModel.addWorkout(
                    Workout(newWorkoutName, newDuration, newDistance, newReps, newSets, exeID)
                )
                passedList!!.add(workoutID)
            }

            // Check if adding a new workout to the list or updating an old one
            exerciseViewModel.updateRoutineWorkoutList(passedRoutineID!!, passedList!!)
        }

        // Delete the workout from the parent routine
        deleteButton.setOnClickListener {
            passedList!!.remove(passedWorkoutID)
            exerciseViewModel.updateRoutineWorkoutList(passedRoutineID!!, passedList)
            finish()
        }

        exerciseViewModel.exercise.observe(this, Observer {
            val exercise = it ?: return@Observer

            // Toggle visibility depending on the exercise's flags
            if (exercise.repsFlag) {
                binding.repsLayout.visibility = View.VISIBLE
            } else {
                binding.repsLayout.visibility = View.GONE
            }
            if (exercise.setsFlag) {
                binding.setsLayout.visibility = View.VISIBLE
            } else {
                binding.setsLayout.visibility = View.GONE
            }
            if (exercise.durationFlag) {
                binding.durationLayout.visibility = View.VISIBLE
            } else {
                binding.durationLayout.visibility = View.GONE
            }
            if (exercise.distanceFlag) {
                binding.distanceLayout.visibility = View.VISIBLE
            } else {
                binding.distanceLayout.visibility = View.GONE
            }

            // Populate the exercise card with information
            Glide.with(this)
                .load(
                    storage.reference
                        .child(ExerciseRepository.EXERCISE_PICTURE)
                        .child(exercise.exerciseID!!)
                )
                .placeholder(R.drawable.glide_placeholder)
                .into(exeCard.exerciseImage)
            exeCard.exerciseNameTextView.text = exercise.name
            exeCard.exerciseTagsTextView.text = exercise.tags.toString()
            exeCard.exerciseDescriptionTextView.text = exercise.description
            exeID = exercise.exerciseID

            saveButton.isEnabled = true
        })

        exerciseViewModel.routineListener.observe(this, Observer {
            val routineListener = it ?: return@Observer

            //spinner.visibility = View.GONE

            if (routineListener.error != null) {
                Log.d(ContentValues.TAG, "Workout saving failed")
                showSnackBar(
                    getString(R.string.exercise_failed),
                    this
                )
            } else {
                Log.d(ContentValues.TAG, "Workout saved, moving back to Routine")
                finish()
            }
            setResult(Activity.RESULT_OK)
        })

    }

    private fun gotoExerciseScreen() {
        launchExerciseSelection.launch(Intent(this, ExerciseActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_IDENTITY = "routine_id"
        const val EXTRA_EXERCISE = "passed exercise ID"
        const val EXTRA_STRING = "workout_name"
        const val EXTRA_DURA = "workout_duration"
        const val EXTRA_DIST = "workout_distance"
        const val EXTRA_REPS = "num_reps"
        const val EXTRA_SETS = "num_sets"
        const val EXTRA_LIST = "workout_list"
        const val PLACEHOLDER_NAME = "New Exercise"
        const val PLACEHOLDER_REPS = 0
        const val PLACEHOLDER_SETS = 0
        const val INT_NULL = -99999
    }
}