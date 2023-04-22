/*
 *  Created by Team Symphony on 4/22/23, 6:21 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 6:05 AM
 */

package com.symphony.mrfit.ui

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.exercise.WorkoutAdapter
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityWorkoutRoutineBinding
import com.symphony.mrfit.ui.Helper.BLANK
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_LIST
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_STRING
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.NEW_ROUTINE
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * Screen for displaying the Workouts belonging to the current Routine
 */

class WorkoutRoutineActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityWorkoutRoutineBinding
    private lateinit var workoutList: RecyclerView
    private lateinit var routineNameText: EditText
    private lateinit var routinePlaylist: EditText
    private lateinit var passedRoutineID: String
    private var routineName: String? = null
    private var playlist: String? = null
    private var exercises: ArrayList<String> = ArrayList()
    private var passedList: ArrayList<String> = ArrayList()

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

    }

    override fun onStart() {
        super.onStart()

        Log.d(ContentValues.TAG, "Activity has (Re)Started")

        // Bind variables to View elements
        val spinner = binding.loadingSpinner
        routineNameText = binding.routineNameEditText
        routinePlaylist = binding.workoutPlaylistEditText
        workoutList = binding.workoutListView
        val startWorkout = binding.startWorkoutButton
        val newExercise = binding.newExerciseButton
        val saveWorkout = binding.saveWorkoutButton
        val deleteWorkout = binding.deleteWorkoutButton
        val playMusic = binding.openPlaylistButton
        val placeholderText = binding.workoutListPlaceholder

        placeholderText.visibility = View.VISIBLE
        workoutList.visibility = View.GONE

        /**
         * Retrieve the extras passed to this intent
         * passedID = The ID of the parent Routine
         * passedName = The Name of the parent Routine
         * passedList = The workoutList from the parent Routine
         * savedName = The Name of the template if it was navigated away from
         * savedMusic = The playlist for the template if it was navigated away from
         */
        passedRoutineID = intent.getStringExtra(EXTRA_IDENTITY)!!
        routineName = intent.getStringExtra(SAVED_NAME)
        playlist = intent.getStringExtra(SAVED_PLAYLIST)

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        workoutList.layoutManager = layoutManager
        exerciseViewModel.getRoutine(passedRoutineID)
        exerciseViewModel.routine.observe(this, Observer {
            val routine = it ?: return@Observer

            if (routineName != null) {
                routineNameText.setText(routineName)
                routinePlaylist.setText(playlist)
            } else {
                routineName = routine.name
            }
            routineNameText.setText(routineName)
            if (playlist != null) {
                routinePlaylist.setText(playlist)
            } else if (routine.playlist != null) {
                playlist = routine.playlist
                routinePlaylist.setText(playlist)
            } else {
                routinePlaylist.setText(BLANK)
            }
            if (routine.workoutList != null) {
                if (routine.workoutList.isNotEmpty()) {
                    exercises = routine.workoutList
                    spinner.visibility = View.VISIBLE
                    passedList = routine.workoutList
                    exerciseViewModel.getWorkouts(exercises)
                }
            }
        })
        exerciseViewModel.workoutList.observe(this, Observer {
            val workList = it ?: return@Observer
            workoutList.adapter = WorkoutAdapter(this, workList, passedRoutineID, passedList)
            placeholderText.visibility = View.GONE
            workoutList.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        })

        // Attempt to launch a playlist
        playMusic.setOnClickListener {
            if (routinePlaylist.text!!.isNotEmpty()) {
                playMusic(routinePlaylist.text.toString())
            } else {
                Helper.showSnackBar(getString(R.string.empty_playlist_warning), this)
            }
        }

        // Start the current workout then save it to the User's history,
        // then return to their Home screen
        startWorkout.setOnClickListener {
            // Check if the routine name is empty
            saveRoutine()
            val intent = Intent(this, CurrentWorkoutActivity::class.java)
            intent.putExtra(EXTRA_STRING, routineName)
            intent.putExtra(EXTRA_ROUTINE, passedRoutineID)
            intent.putExtra(EXTRA_LIST, passedList)
            startActivity(intent)
        }

        // Navigate to a screen to make a new workout
        newExercise.setOnClickListener {
            val intent = Intent(this, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_ROUTINE, passedRoutineID)
            intent.putExtra(EXTRA_IDENTITY, NEW_ID)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_STRING, getText(R.string.new_exercise))
            intent.putExtra(WorkoutTemplateActivity.EXTRA_LIST, passedList)
            startActivity(intent)
        }

        // Save the current Routine and go back to the user's Home screen
        saveWorkout.setOnClickListener {
            saveRoutine()
            finish()
        }

        // Remove the current workout from the user's list
        deleteWorkout.setOnClickListener {
            exerciseViewModel.deleteRoutine(passedRoutineID)
            Toast.makeText(
                applicationContext,
                "This workout has been removed from your list",
                Toast.LENGTH_SHORT
            ).show()
            finish()

        }
    }

    override fun onPause() {
        super.onPause()
        tempSave()
    }

    private fun tempSave() {
        val n: String? = if (routineNameText.text.isNotEmpty())
            routineNameText.text.toString()
        else
            routineName
        val p: String? = if (routinePlaylist.text.isNotEmpty())
            routinePlaylist.text.toString()
        else
            playlist
        intent.putExtra(SAVED_NAME, n)
        intent.putExtra(SAVED_PLAYLIST, p)
    }

    /**
     * Save the current routine
     */
    private fun saveRoutine() {
        routineName = NEW_ROUTINE
        if (routineNameText.text.isNotEmpty()) {
            routineName = routineNameText.text.toString()
        }
        var newRoutinePlaylist = BLANK
        if (routinePlaylist.text!!.isNotEmpty()) {
            newRoutinePlaylist = routinePlaylist.text.toString()
        }
        exerciseViewModel.updateRoutine(routineName!!, newRoutinePlaylist, passedRoutineID)
    }

    /**
     * Attempt to link to a music service
     * Parse the text in the playlist field for which service to use
     */
    private fun playMusic(playlist: String) {
        // Check if the linked playlist is from Youtube
        if (playlist.contains("youtube")) {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playlist))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playlist))
            try {
                startActivity(appIntent)
            } catch (e: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        } else if (playlist.contains("spotify")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playlist))
            startActivity(intent)
        } else {
            // Unrecognized playlist, do nothing
            Toast.makeText(this, "Could not parse playlist link", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ROUTINE = "passed routine id"
        const val NEW_ID = "NEW"
        const val SAVED_NAME = "saved template name"
        const val SAVED_PLAYLIST = "saved playlist"
    }

}