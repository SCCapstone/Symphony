package com.symphony.mrfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.databinding.ActivityWorkoutsBinding

/**
 * TODO: Classes should be Capitalized, variables should be named using camelCase
 */

class AddWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind variables to View elements
        val addWorkoutBtn = binding.addworkout
        val workoutName = binding.workoutname
        val workoutDesc = binding.WorkoutDescription

        // Talk to Firestore when clicked
        addWorkoutBtn.setOnClickListener() {
            val database = Firebase.firestore
            database.collection("exercises").document("replace me").get()
                .addOnSuccessListener { documentSnapshot ->
                    /**
                     * TODO: Do something with the documentSnapshot
                     */
                }
        }

    }
}