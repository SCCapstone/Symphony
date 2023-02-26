/*
 *  Created by Team Symphony on 2/26/23, 9:27 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 9:27 AM
 */

package com.symphony.mrfit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.databinding.ActivityAddStockExercisesBinding

class AddStockExercises : AppCompatActivity() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityAddStockExercisesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStockExercisesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        val spinner = binding.loadingSpinner
        val text = binding.addingExercisesTextView

        exerciseViewModel.addExercise(
            "Walking",
            "A brisk walk anywhere, anytime.",
            arrayListOf<String>(
                "Low-Intensity",
                "Aerobic",
                "Cardio",
                "At Home"
            )
        )
    }
}