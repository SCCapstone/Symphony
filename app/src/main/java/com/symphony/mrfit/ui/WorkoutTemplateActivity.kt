package com.symphony.mrfit.ui

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.databinding.ActivityWorkoutTemplateBinding
import java.io.File


class WorkoutTemplateActivity : AppCompatActivity() {

    private lateinit var exerciseViewModel: ExerciseViewModel
    lateinit var binding: ActivityWorkoutTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        val workoutName = binding.editWorkOutName
        val weight = binding.editWeight
        val reps = binding.editReps
        val pickExe = binding.pickExerciseButton
        val fileName = "app/java/workout.txt"
        val file = File(fileName)
        var passedID: String? = null

        if (intent.getStringExtra(EXTRA_ID) != "null") {
            passedID = intent.getStringExtra(EXTRA_ID)
        }
        val passedName = intent.getStringExtra(EXTRA_STRING)
        val passedRep = intent.getStringExtra(EXTRA_REPS)
        val passedList = intent.getStringArrayListExtra(EXTRA_LIST)

        workoutName.setText(passedName)
        reps.setText(passedRep)

        pickExe.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            var newWorkoutName: String? = null
            if(workoutName.text.isNotEmpty()) { newWorkoutName = workoutName.text.toString()}
            var newWeight: String? = null
            if(weight.text.isNotEmpty()) { newWeight = weight.text.toString() }
            var newReps: String? = null
            if(reps.text.isNotEmpty()) { newReps = reps.text.toString() }

            val workouts = "Today's Workout$newWorkoutName,$newWeight,$newReps,"
            //file.writeText(workouts)

            // Add a workout to the database
            val temp = newReps?.toInt()
            val a = newWorkoutName ?: "Empty"
            val b = temp ?: 0
            val c = "ABCD12345"
            passedList!!.add(c)
            exerciseViewModel.addWorkout((Workout(a,b,c)), "ABCD123456")
            exerciseViewModel.addWorkoutToRoutine(passedID, passedList)
            finish()
        }

    }

    companion object {
        const val EXTRA_STRING = "workout_name"
        const val EXTRA_REPS = "num_reps"
        const val EXTRA_LIST = "workout_list"
    }
}