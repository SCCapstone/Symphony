package com.symphony.mrfit.ui

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.WorkoutAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutRoutineBinding
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_LIST
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_STRING

/**
 * TODO: Classes should be Capitalized, variables should be named using camelCase
 */

class WorkoutRoutineActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityWorkoutRoutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        // Bind variables to View elements
        val workoutName = binding.routineNameEditText
        val workoutDesc = binding.workoutDescriptionEditText
        val workoutList = binding.workoutListView
        val startWorkout = binding.startWorkoutButton
        val newWorkout = binding.newExerciseButton

        val passedName = intent.extras!!.getString(EXTRA_STRING)
        val passedList = intent.extras!!.getStringArrayList(EXTRA_LIST)

        /**
         * Set the layout of the list of workouts presented to the user
         */
        layoutManager = LinearLayoutManager(this)
        workoutList.layoutManager = layoutManager

        /**
         * Populate the list with the workouts associated with this routine
         */
        workoutName.setText(passedName)
        if (passedList != null) {
            exerciseViewModel.getWorkouts(passedList)
        }
        exerciseViewModel.workoutList.observe(this, Observer {
            val workList = it ?: return@Observer

            workoutList.adapter = WorkoutAdapter(this, workList)
        })

        /**
         * Start the current workout then save it to the User's history
         */
        startWorkout.setOnClickListener {
            // profileViewModel.addWorkoutToHistory()
            Toast.makeText(
                applicationContext,
                "Your workout has been saved to your history",
                Toast.LENGTH_LONG
            ).show()
        }

        /**
         * Navigate to a screen to make a new workout
         */
        newWorkout.setOnClickListener {
            val intent = Intent(this, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_ID, "null")
            startActivity(intent)
        }
    }

}