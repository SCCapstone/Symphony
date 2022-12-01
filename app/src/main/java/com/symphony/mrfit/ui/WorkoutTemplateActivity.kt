package com.symphony.mrfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.databinding.ActivityWorkoutTemplateBinding


class WorkoutTemplateActivity : AppCompatActivity() {

    lateinit var binding: ActivityWorkoutTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workoutName = binding.editWorkOutName
        val weight = binding.editWeight
        val reps = binding.editReps

        binding.button2.setOnClickListener {
            var newWorkoutName: String? = null
            if(workoutName.text.isNotEmpty()) { newWorkoutName = workoutName.text.toString()}
            var newWeight: String? = null
            if(weight.text.isNotEmpty()) { newWeight = weight.text.toString() }
            var newReps: String? = null
            if(reps.text.isNotEmpty()) { newReps = reps.text.toString() }
        }
    }
}