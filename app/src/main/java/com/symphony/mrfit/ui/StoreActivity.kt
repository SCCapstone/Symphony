package com.symphony.mrfit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import com.symphony.mrfit.R


class StoreActivity {
    lateinit var workoutTemplate: WorkoutTemplate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_store)
        val saveButton : Button = findViewByID(R.id.ExportButton)
    }
}