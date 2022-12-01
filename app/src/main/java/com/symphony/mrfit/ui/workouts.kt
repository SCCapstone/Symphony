package com.symphony.mrfit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.core.View
import com.symphony.mrfit.R
import android.view.View.OnClickListener
import com.google.firebase.database.FirebaseDatabase
import com.symphony.mrfit.data.workoutdata.workouthelper

class workouts : AppCompatActivity(), View.OnClickListener{
    //private vars for name and description of workouts
    private var workoutName : TextInputLayout?= null
    private var workoutDescription : TextInputLayout?=null

    //private late init for button
    private lateinit var addWorkoutBtn : Button

    lateinit var rootNode: FirebaseAuth
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        //set to input in view
        workoutDescription = findViewById(R.id.WorkoutDescription)
        workoutName = findViewById(R.id.workoutname)

        //set button from view
        addWorkoutBtn = findViewById(R.id.addworkout)

        //cant get button to function weirdly
        addWorkoutBtn.setOnClickListener(this)

    }
     fun onClick(v: View?)
    {
        when(v) {
            addWorkoutBtn-> {
                rootNode = FirebaseAuth.getInstance()
                reference = rootNode.getReference("exercises")

                var name = workoutName?.editTest!!.toString()
                var descrip = workoutDescription?.editTest!!.toString()

                val helperClass: workouthelper = workouthelper(name,descrip)
                reference.child(name).setValue(helperClass)
            }

        }

    }
}