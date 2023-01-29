/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.ExerciseAdapter
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        val search = binding.exerciseSearchButton
        val searchBar = binding.exerciseSearchEditText
        val exeList = binding.exerciseListView

        /**
         * Set the layout of the list of exercises presented to the user
         */
        layoutManager = LinearLayoutManager(this)
        exeList.layoutManager = layoutManager

        /**
         * Observe the current list of exercises and update the UI accordingly
         */
        exerciseViewModel.getExercisesBySearch("")
        exerciseViewModel.exerciseList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating exercise list")
            val exerciseList = it ?: return@Observer

            exeList.adapter = ExerciseAdapter(this, exerciseList)
        })

        search.setOnClickListener {
            exerciseViewModel.getExercisesBySearch(searchBar.text.toString())
        }
    }
}