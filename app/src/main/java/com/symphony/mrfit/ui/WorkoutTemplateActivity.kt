package com.symphony.mrfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.symphony.mrfit.R
import com.symphony.mrfit.databinding.ActivityWorkoutTemplateBinding


class WorkoutTemplateActivity {

    lateinit var binding: ActivityWorkoutTemplate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val workoutName = binding.editWorkOutName
        val weight = binding.editWeight
        val reps = binding.editWeigth

        binding.button2.setOnClickListener {
            var newWorkoutName: String? = null
            if(workoutName.text.isNotEmpty()) { newWorkoutName = workoutName.text.toString()}
            var newWeight: String? = null
            if(weight.text.isNotEmpty()) { newWeight = weight.text.toInt() }
            var newReps: String? = null
            if(reps.text.isNotEmpty()) { newReps = reps.text.toInt() }
        }
    }
}