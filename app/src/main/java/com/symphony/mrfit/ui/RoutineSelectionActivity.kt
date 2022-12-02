/*
 * Created by Team Symphony 11/28/22, 8:27 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 8:27 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.WorkoutAdapter
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.databinding.ActivityRoutineSelectionBinding

class RoutineSelectionActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityRoutineSelectionBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoutineSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        val newWorkout = binding.newWorkoutButton
        val workList = binding.workoutListView

        /**
         * Set the layout of the grid of workout templates presented to the user
         */
        layoutManager = GridLayoutManager(this,2)
        workList.layoutManager = layoutManager

        /**
         * Initialize the workout list, then listen to it to update the UI
         */
        exerciseViewModel.getUserWorkouts()
        exerciseViewModel.workoutList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating workout list")
            val workoutList = it ?: return@Observer

            workList.adapter = WorkoutAdapter(this, workoutList)
        })

        newWorkout.setOnClickListener {
            val workout = Workout("New Workout")
            newWorkout(workout)
        }
    }

    private fun newWorkout(workout: Workout) {
        val intent = Intent(this, WorkoutRoutineActivity::class.java)
        intent.putExtra(Companion.EXTRA_STRING,workout.name)
        intent.putExtra(Companion.EXTRA_LIST,workout.workoutList)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_STRING = "passed workout name"
        const val EXTRA_LIST = "passed workout exercises"
    }
}