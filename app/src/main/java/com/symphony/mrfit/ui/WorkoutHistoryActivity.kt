/*
 *  Created by Team Symphony on 4/1/23, 11:17 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 11:17 PM
 */

package com.symphony.mrfit.ui

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.HistoryAdapter2
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutHistoryBinding
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY
import java.text.SimpleDateFormat
import java.util.*

class WorkoutHistoryActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var binding: ActivityWorkoutHistoryBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val historyList = binding.historyList
        val spinner = binding.loadingSpinner

        fun deleteHistory(historyID: String) {
            profileViewModel.deleteWorkoutFromHistory(historyID)
        }

        fun openHistory(history: History) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_workout_history)
            dialog.setTitle("Workout History")

            val name = dialog.findViewById<TextView>(R.id.workoutHistoryName)
            val startMessage = dialog.findViewById<TextView>(R.id.historyStartedTextView)
            val endMessage = dialog.findViewById<TextView>(R.id.historyEndedTextView)
            val openRoutine = dialog.findViewById<Button>(R.id.openHistoryRoutineButton)
            val ok = dialog.findViewById<Button>(R.id.okHistoryButton)

            name.text = history.name

            startMessage.text = SimpleDateFormat(
                "'Started at' MMMM dd, yyyy 'at' hh:mm a",
                Locale.getDefault()
            )
                .format(history.date!!.toDate().time - history.duration!!)
            endMessage.text = SimpleDateFormat(
                "'Finished at' MMMM dd, yyyy 'at' hh:mm a",
                Locale.getDefault()
            )
                .format(history.date.toDate())


            openRoutine.setOnClickListener {
                val intent = Intent(this, WorkoutRoutineActivity::class.java)
                intent.putExtra(EXTRA_IDENTITY, history.routine)
                startActivity(intent)
                dialog.dismiss()
                this.finish()
            }

            ok.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        spinner.visibility = View.VISIBLE

        //Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        historyList.layoutManager = layoutManager

        profileViewModel.getWorkoutHistory()
        profileViewModel.workoutHistory.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val workoutHistory = it ?: return@Observer

            historyList.adapter =
                HistoryAdapter2(this, workoutHistory, ::deleteHistory, ::openHistory)
            spinner.visibility = View.GONE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}