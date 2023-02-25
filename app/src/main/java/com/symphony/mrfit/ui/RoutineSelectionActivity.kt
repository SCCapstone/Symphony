/*
 *  Created by Team Symphony on 2/25/23, 1:42 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/25/23, 1:42 AM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.RoutineAdapter
import com.symphony.mrfit.databinding.ActivityRoutineSelectionBinding
import com.symphony.mrfit.ui.Helper.BLANK
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * Screen for displaying the current User's saved Workout Routines
 */

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
    }

    override fun onStart() {
        super.onStart()

        val screen = binding.selectionScreenView
        val spinner = binding.loadingSpinner
        val newRoutine = binding.newWorkoutButton
        val routineList = binding.routineListView

        // Hide the screen till done loading
        spinner.visibility = View.VISIBLE

        // Set the layout of the grid of routines presented to the user
        layoutManager = GridLayoutManager(this, 2)
        routineList.layoutManager = layoutManager

        // Initialize the routine list, then listen to it to update the UI
        exerciseViewModel.getUserRoutines()
        exerciseViewModel.workoutRoutineList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating routine list")
            val workoutRoutineList = it ?: return@Observer

            routineList.adapter = RoutineAdapter(this, workoutRoutineList)
            spinner.visibility = View.GONE

        })


        newRoutine.setOnClickListener {
            newRoutine()
        }
    }

    private fun newRoutine() {
        val intent = Intent(this, WorkoutRoutineActivity::class.java)
        val routineID = exerciseViewModel.addRoutine(NEW_ROUTINE, BLANK, ArrayList())
        intent.putExtra(EXTRA_IDENTITY, routineID)
        intent.putExtra(EXTRA_STRING,NEW_WORKOUT)
        intent.putExtra(EXTRA_LIST,ArrayList<String>())
        startActivity(intent)
    }

    companion object {
        const val EXTRA_STRING = "passed routine name"
        const val EXTRA_LIST = "passed routine exercises"
        const val NEW_ROUTINE = "New Routine"
        const val NEW_WORKOUT = "New Workout"
        const val NEW_ID = "NEW"
    }
}