/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
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
    lateinit var binding: ActivityWorkoutTemplateBinding
    private val launchExerciseSelection =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                exerciseViewModel.getExercise(result.data?.getStringExtra(EXTRA_IDENTITY)!!)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val workoutName = binding.editWorkOutName
        val weight = binding.editWeight
        val reps = binding.editReps
        val pickExe = binding.pickExerciseButton
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
        val passedName: String? = intent.getStringExtra(EXTRA_STRING)
        val passedRep: String? = intent.getStringExtra(EXTRA_REPS)
        val passedList: ArrayList<String>? = intent.getStringArrayListExtra(EXTRA_LIST)

        workoutName.setText(passedName)
        reps.setText(passedRep.toString())
        if (passedWorkoutID != NEW_ID) {
            deleteButton.visibility = View.VISIBLE
            exerciseViewModel.getExercise(intent.getStringExtra(EXTRA_EXERCISE)!!)
        }

        //Launch the Exercise selection activity and await its return
        pickExe.setOnClickListener {
            launchExerciseSelection.launch(Intent(this, ExerciseActivity::class.java))
        }

        // Exercise Card should have same functionality as pickExe button
        exeCard.root.setOnClickListener {
            launchExerciseSelection.launch(Intent(this, ExerciseActivity::class.java))
        }

        // Save the workout and return to the parent Routine
        saveButton.setOnClickListener {
            var newWorkoutName: String = PLACEHOLDER_NAME
            if(workoutName.text.isNotEmpty()) { newWorkoutName = workoutName.text.toString()}
            var newWeight: String = PLACEHOLDER_WEIGHT
            if(weight.text.isNotEmpty()) { newWeight = weight.text.toString() }
            var newReps: Int = PLACEHOLDER_REPS
            if(reps.text.isNotEmpty()) { newReps = reps.text.toString().toInt() }

            //val workouts = "Today's Workout$newWorkoutName,$newWeight,$newReps,"
            //file.writeText(workouts)

            if (passedWorkoutID != NEW_ID) {
                /**
                 * TODO: If a field is left blank when updating a workout, preserve the old data
                 */
                // Update a workout in the database
                exerciseViewModel.updateWorkout(Workout(newWorkoutName,newReps, exeID, passedWorkoutID))
            } else {
                // Add a workout to the database
                val workoutID = exerciseViewModel.addWorkout(Workout(newWorkoutName, newReps, exeID))
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
            val exercise = it?: return@Observer

            pickExe.visibility = View.GONE
            exeCard.root.visibility = View.VISIBLE

            //exeCard.exerciseImage = exercise.Image
            exeCard.exerciseNameTextView.text = exercise.name
            exeCard.exerciseDescriptionTextView.text = exercise.tags.toString()
            exeID = exercise.exerciseID
        })

        exerciseViewModel.routineListener.observe(this, Observer {
            val routineListener = it ?: return@Observer

            //spinner.visibility = View.GONE

            if (routineListener.error != null) {
                Log.d(ContentValues.TAG, "Workout saving failed")
                showSnackBar(
                    "Attempt to save workout failed, try again",
                    this
                )
            } else {
                Log.d(ContentValues.TAG, "Workout saved, moving back to Routine")
                finish()
            }
            setResult(Activity.RESULT_OK)
        })

    }

    companion object {
        const val EXTRA_IDENTITY = "routine_id"
        const val EXTRA_EXERCISE = "passed exercise ID"
        const val EXTRA_STRING = "workout_name"
        const val EXTRA_REPS = "num_reps"
        const val EXTRA_LIST = "workout_list"
        const val PLACEHOLDER_NAME = "New Workout"
        const val PLACEHOLDER_REPS = 0
        const val PLACEHOLDER_WEIGHT = "0"
    }
}