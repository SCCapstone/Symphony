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
import com.symphony.mrfit.data.exercise.RoutineAdapter
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.data.model.WorkoutRoutine
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

        val newRoutine = binding.newWorkoutButton
        val routineList = binding.routineListView

        /**
         * Set the layout of the grid of routines presented to the user
         */
        layoutManager = GridLayoutManager(this,2)
        routineList.layoutManager = layoutManager

        /**
         * Initialize the routine list, then listen to it to update the UI
         */
        exerciseViewModel.getUserRoutines()
        exerciseViewModel.workoutRoutineList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating routine list")
            val workoutRoutineList = it ?: return@Observer

            routineList.adapter = RoutineAdapter(this, workoutRoutineList)
        })

        newRoutine.setOnClickListener {
            val workoutRoutine = WorkoutRoutine("New Workout")
            newRoutine(workoutRoutine)
        }

        exerciseViewModel.addWorkout(Workout("2 mile walk",0,"ABCD1"), "ABCD1")
        exerciseViewModel.addWorkout(Workout("4 mile walk",0,"ABCD12"), "ABCD12")
        exerciseViewModel.addWorkout(Workout("6 mile walk",0,"ABCD123"), "ABCD123")
        exerciseViewModel.addWorkout(Workout("Sprint",0,"ABCD1234"), "ABCD1234")
        exerciseViewModel.addWorkout(Workout("Nap",0,"ABCD12345"), "ABCD12345")
    }

    private fun newRoutine(workoutRoutine: WorkoutRoutine) {
        val intent = Intent(this, WorkoutRoutineActivity::class.java)
        intent.putExtra(Companion.EXTRA_STRING,workoutRoutine.name)
        intent.putExtra(Companion.EXTRA_LIST,workoutRoutine.workoutList)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_STRING = "passed routine name"
        const val EXTRA_LIST = "passed routine exercises"
    }
}