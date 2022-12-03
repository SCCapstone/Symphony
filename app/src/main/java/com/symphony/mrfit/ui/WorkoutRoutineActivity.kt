package com.symphony.mrfit.ui

import android.content.Intent
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
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

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
        val newExercise = binding.newExerciseButton

        val passedID = intent.extras!!.getString(EXTRA_IDENTITY)
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

            workoutList.adapter = WorkoutAdapter(this, workList, passedID!!, passedList!!)
        })

        /**
         * Start the current workout then save it to the User's history
         */
        startWorkout.setOnClickListener {
            // profileViewModel.addWorkoutToHistory()
            exerciseViewModel.updateRoutine(workoutName.text.toString(), passedID!!)
            Toast.makeText(
                applicationContext,
                "Your workout has been saved to your history",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        /**
         * Navigate to a screen to make a new workout
         */
        newExercise.setOnClickListener {
            val intent = Intent(this, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_IDENTITY, passedID)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_STRING,"New Workout")
            intent.putExtra(WorkoutTemplateActivity.EXTRA_REPS,"0")
            intent.putExtra(WorkoutTemplateActivity.EXTRA_LIST, passedList)
            startActivity(intent)
        }
    }

}