package com.symphony.mrfit.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.databinding.ActivityWorkoutsBinding

/**
 * TODO: Classes should be Capitalized, variables should be named using camelCase
 */

class AddWorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutsBinding

    //displays workout list


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bind variables to View elements
        val addWorkoutBtn = binding.addworkout
        val workoutName = binding.workoutname
        val workoutDesc = binding.WorkoutDescription


        // save to Firestore when clicked
        addWorkoutBtn.setOnClickListener() {
            //val database = Firebase.firestore
            //database.collection("exercises").document("replace me").get()
            //.addOnSuccessListener { documentSnapshot ->
//                    /**
//                     * ODO: Do something with the documentSnapshot
//                     */
//                }
            val workName = workoutName.text.toString()
            val desc = workoutDesc.text.toString()

            saveFireStore(workName,desc)
        }
        readData()
//        val database= Firebase.firestore
//        database.collection("workout").document("userworkouts")
//            .collection("workout").get()
//            .addOnCompleteListener{
//                val result = StringBuffer()
//
//                    for(document in it.result!!) {
//                        result.append(document.data.getValue("name")).append(" ")
//                            .append(document.data.getValue("description")).append("\n\n")
//                    }
//                workoutList.text = result
//
//            }
    }
        //method to save to firestore
        fun saveFireStore(workName:String,desc:String){
            val db = FirebaseFirestore.getInstance()
            val workout: MutableMap<String,Any> =HashMap()
                workout["name"]=workName
                workout["description"]=desc

            db.collection("workout").document("userworkouts").collection("workout").add(workout)
                .addOnSuccessListener {
                    Toast.makeText(this@AddWorkoutActivity, "successfully added workout",
                        Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@AddWorkoutActivity, "failure to add workout",
                        Toast.LENGTH_SHORT).show()
                }
            readData()
        }
        fun readData(){
            val workoutList = binding.WorkoutList
            val database= Firebase.firestore
            database.collection("workout").document("userworkouts")
                .collection("workout").get()
                .addOnCompleteListener{
                    val name ="Workout Name: "
                    val desc = "Workout Description: "
                    val result = StringBuffer()

                    for(document in it.result!!) {
                        result.append(name).append(document.data.getValue("name")).append(" ")
                            .append(desc).append(document.data.getValue("description")).append("\n\n")
                    }
                workoutList.text = result

            }
        }


}