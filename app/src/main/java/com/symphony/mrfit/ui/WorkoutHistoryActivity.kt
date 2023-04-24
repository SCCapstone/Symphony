/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:50 AM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.symphony.mrfit.R
import com.symphony.mrfit.data.adapters.HistoryAdapter2
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutHistoryBinding
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Screen for the User to manage their workout History
 */

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
        val manualHistory = binding.addHistoryButton
        val spinner = binding.loadingSpinner

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

        manualHistory.setOnClickListener {
            newHistory()
        }
    }

    /**
     * Remove the associated History from the database
     */
    private fun deleteHistory(historyID: String) {
        profileViewModel.deleteWorkoutFromHistory(historyID)
    }

    /**
     * Create a dialog for more detail on the associated History
     * Allow the user to navigate directly to its Template if desired
     */
    private fun openHistory(history: History) {
        // Create the dialog and inflate its view like an activity
        val materialDialog = MaterialAlertDialogBuilder(this)
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.popup_workout_history, null, false)

        materialDialog.setView(dialogView)

        val name = dialogView.findViewById<TextView>(R.id.workoutHistoryName)
        val startMessage = dialogView.findViewById<TextView>(R.id.historyStartedTextView)
        val endMessage = dialogView.findViewById<TextView>(R.id.historyEndedTextView)

        // Populate the dialog with appropriate info from the History
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

        // Navigate to the associated Template
        // Finish this activity so a back press returns to the User's Profile
        materialDialog.setNeutralButton(getString(R.string.button_open_routine)) { dialog, _ ->
            val intent = Intent(this, WorkoutRoutineActivity::class.java)
            intent.putExtra(EXTRA_IDENTITY, history.routine)
            startActivity(intent)
            dialog.dismiss()
            this.finish()
        }

        materialDialog.setPositiveButton(getString(R.string.button_ok)) { dialog, _ ->
            dialog.dismiss()
        }

        materialDialog.show()
    }

    /**
     * Navigate to a screen to manually add a History to the User's database
     */
    private fun newHistory() {
        val intent = Intent(this, ManualWorkoutActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}