package com.symphony.mrfit.ui

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Button
import com.symphony.mrfit.R
import com.symphony.mrfit.databinding.StoreActivity
import com.google.firebase.ktx.Firebase


class StoreActivity {
    lateinit var workoutTemplate: WorkoutTemplateActivity
    lateinit var binding: StoreActivity

    oveerride fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_store)
        ExportButton.setOnClickListener() {
            val db = Firebase.firestore
            Firestore.collection("WorkoutName").documents("New").set(Workout)
                .addOnSucessListener{ result ->
                    Log.d(TAG, "Export")
                }
                .addOnFailsureListner { exception ->
                    Log.w(TAG, "Did not work", exception)
                }
        }
    }

    interface ExportButton {

    }
}