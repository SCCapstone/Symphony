/*
 *  Created by Team Symphony on 4/2/23, 4:31 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 4:31 AM
 */

package com.symphony.mrfit

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.databinding.ActivityAddStockExercisesBinding
import com.symphony.mrfit.ui.ExerciseActivity

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

        /**
         * TODO: Replace all Uri parses with appropriate images
         */

        exerciseViewModel.addExercise(
            Exercise(
                "Walking",
                "A brisk walk anywhere, anytime.",
                arrayListOf(
                    "Low-Intensity",
                    "Aerobic",
                    "Cardio",
                    "At Home"
                ),
                repsFlag = false,
                setsFlag = false,
                distanceFlag = true,
                durationFlag = true
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Jogging",
                "Trotting or running at a slow or leisurely pace.",
                arrayListOf(
                    "Low-Intensity",
                    "Aerobic",
                    "Cardio",
                    "At Home"
                ),
                repsFlag = false,
                setsFlag = false,
                distanceFlag = true,
                durationFlag = true
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Running",
                "Rapidly propelling yourself forward on foot.",
                arrayListOf(
                    "High-Intensity",
                    "Aerobic",
                    "Cardio"
                ),
                repsFlag = false,
                setsFlag = false,
                distanceFlag = true,
                durationFlag = true
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Squats",
                "Lowers your hips from a standing position and then stands back up.",
                arrayListOf(
                    "Lower body",
                    "Strength training",
                    "Calisthenics",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Lunges",
                "Pose with one leg forward with knee bent and foot flat on the ground while the other leg is positioned behind",
                arrayListOf(
                    "Lower body",
                    "Legs",
                    "Strength training",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Push-Ups",
                "Raising and lowering the body using the arms.",
                arrayListOf(
                    "Upper body",
                    "Calisthenics",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Sit-Ups",
                "Lie on your back with legs bent, then bend at the waist and move their head and torso towards your legs.",
                arrayListOf(
                    "Core",
                    "Lower body",
                    "Calisthenics",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Crunches",
                "Lie on your back with legs bent, then bend at the waist and move their head and torso towards your legs.",
                arrayListOf(
                    "Core",
                    "Calisthenics",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Dumbbell press",
                "",
                arrayListOf(
                    "Upper body",
                    "Strength training",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )

        exerciseViewModel.addExercise(
            Exercise(
                "Dumbbell row",
                "",
                arrayListOf(
                    "Upper body",
                    "Strength training",
                    "At home"
                ),
                repsFlag = true,
                setsFlag = true,
                distanceFlag = false,
                durationFlag = false
            ),
            Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
        )
    }
}