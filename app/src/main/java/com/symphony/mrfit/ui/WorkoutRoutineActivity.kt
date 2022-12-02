package com.symphony.mrfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutsBinding

/**
 * TODO: Classes should be Capitalized, variables should be named using camelCase
 */

class WorkoutRoutineActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityWorkoutsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]

        // Bind variables to View elements
        val workoutName = binding.workoutNameEditText
        val workoutDesc = binding.workoutDescriptionEditText
        val workoutList = binding.workoutList
        val startWorkout = binding.startWorkoutButton
        val newWorkout = binding.newWorkoutButton

        /**
         * Will be depreciated when Observer is implemented
         */
        fun readData() {
            val database = Firebase.firestore
            database.collection("workout").document("userworkouts")
                    .collection("workout").get()
                    .addOnCompleteListener {
                        val name = "Workout Name: "
                        val desc = "Workout Description: "
                        val result = StringBuffer()

                        for (document in it.result!!) {
                            result.append(name).append(document.data.getValue("name")).append(" ")
                                    .append(desc).append(document.data.getValue("description"))
                                    .append("\n\n")
                        }
                        workoutList.text = result

                    }
        }

        readData()

        /**
         * Populate the list with the exercises? associated with this workout
         */
        val DELETE_ME = ArrayList<String>()
        exerciseViewModel.getExercisesByWorkout(DELETE_ME)
        exerciseViewModel.exerciseList.observe(this, Observer {
            /**
             * TODO: Update the UI when a new (Workout or Exercise?) is added to the list
             */
        })

        /**
         * Start the current workout then save it to the User's history
         */
        startWorkout.setOnClickListener {
            profileViewModel.addWorkoutToHistory()
        }

        /*
        newWorkout.setOnClickListener() {
            val workName = workoutName.text.toString()
            val desc = workoutDesc.text.toString()
            saveFireStore(workName,desc)

            exerciseViewModel.addWorkout("New Workout", null)
        }

         */

        /**
         * Depreciated by startWorkout's OnClickListener
         */
        /*
        fun saveFireStore(workName: String, desc: String) {
            val db = FirebaseFirestore.getInstance()
            val workout: MutableMap<String, Any> = HashMap()
            workout["name"] = workName
            workout["description"] = desc

            db.collection("workout").document("userworkouts").collection("workout").add(workout)
                .addOnSuccessListener {
                    Toast.makeText(
                        this@AddWorkoutActivity, "successfully added workout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@AddWorkoutActivity, "failure to add workout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            readData()
        }

         */
    }

}