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
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.databinding.ActivityWorkoutTemplateBinding
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.NEW_ID
import com.symphony.mrfit.ui.WorkoutRoutineActivity.Companion.EXTRA_ROUTINE
import java.io.File

/**
 * View Class for modifying a Workout
 */

class WorkoutTemplateActivity : AppCompatActivity() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    lateinit var binding: ActivityWorkoutTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        val workoutName = binding.editWorkOutName
        val weight = binding.editWeight
        val reps = binding.editReps
        val pickExe = binding.pickExerciseButton
        val fileName = "app/java/workout.txt"
        val file = File(fileName)

        /**
         * Retrieve the extras passed to this intent
         * passedWorkoutID = The ID of the current workout
         * passedName = The Name of the Workout
         * passedRep = The NumberOfReps from the Workout
         * passedList = The workoutList from the parent Routine
         */

        Log.d(ContentValues.TAG, "Inside routine ${intent.getStringExtra(EXTRA_ROUTINE)}")
        val passedRoutineID = intent.getStringExtra(EXTRA_ROUTINE)
        val passedWorkoutID = intent.getStringExtra(EXTRA_IDENTITY)
        val passedName: String? = intent.getStringExtra(EXTRA_STRING)
        val passedRep: String? = intent.getStringExtra(EXTRA_REPS)
        val passedList: ArrayList<String>? = intent.getStringArrayListExtra(EXTRA_LIST)

        workoutName.setText(passedName)
        reps.setText(passedRep.toString())

        pickExe.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            var newWorkoutName: String = PLACEHOLDER_NAME
            if(workoutName.text.isNotEmpty()) { newWorkoutName = workoutName.text.toString()}
            var newWeight: String = PLACEHOLDER_WEIGHT
            if(weight.text.isNotEmpty()) { newWeight = weight.text.toString() }
            var newReps: Int = PLACEHOLDER_REPS
            if(reps.text.isNotEmpty()) { newReps = reps.text.toString().toInt() }

            //val workouts = "Today's Workout$newWorkoutName,$newWeight,$newReps,"
            //file.writeText(workouts)

            val workoutID = if (passedWorkoutID != NEW_ID) {
                /**
                 * TODO: If a field is left blank when updating a workout, preserve the old data
                 */
                // Update a workout in the database
                exerciseViewModel.updateWorkout(Workout(newWorkoutName,newReps, newWeight, passedWorkoutID))
                passedWorkoutID!!
            } else {
                // Add a workout to the database
                exerciseViewModel.addWorkout(Workout(newWorkoutName, newReps, newWeight))
            }

            /**
             * TODO: Add the new/updated workout to the current Routine's list
             */

            passedList!!.add(workoutID)
            exerciseViewModel.addWorkoutToRoutine(passedRoutineID, passedList)

            Toast.makeText(
                applicationContext,
                "Your intent to create a workout has been recognized",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

    }

    companion object {
        const val EXTRA_IDENTITY = "routine_id"
        const val EXTRA_STRING = "workout_name"
        const val EXTRA_REPS = "num_reps"
        const val EXTRA_LIST = "workout_list"
        const val PLACEHOLDER_NAME = "New Workout"
        const val PLACEHOLDER_REPS = 0
        const val PLACEHOLDER_WEIGHT = "0"
    }
}