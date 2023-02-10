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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.RoutineAdapter
import com.symphony.mrfit.databinding.ActivityRoutineSelectionBinding
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * View Class for display the current User's saved Workout Routines
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
            newRoutine()
        }
    }

    /**
     * TODO: Replace placeholder material
     */
    private fun newRoutine() {
        val intent = Intent(this, WorkoutRoutineActivity::class.java)
        val routineID = exerciseViewModel.addRoutine(NEW_ROUTINE,ArrayList())
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