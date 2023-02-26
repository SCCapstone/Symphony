/*
 *  Created by Team Symphony on 2/26/23, 3:04 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 3:04 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseAdapter
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.databinding.ActivityExerciseBinding

/**
 * Screen for the User to select an Exercise to pair with the parent Workout.
 * The user should also be able to enter a search term to narrow down possible Exercises.
 */

class ExerciseActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val spinner = binding.loadingSpinner
        val search = binding.exerciseSearchButton
        val searchBar = binding.exerciseSearchEditText
        val exeList = binding.exerciseListView
        val newExe = binding.button2

        // Set the layout of the list of exercises presented to the user
        layoutManager = LinearLayoutManager(this)
        exeList.layoutManager = layoutManager

        // Observe the current list of exercises and update the UI accordingly
        exerciseViewModel.getExercisesBySearch("")
        exerciseViewModel.exerciseList.observe(this, Observer {
            Log.d(ContentValues.TAG, "Updating exercise list")
            val exerciseList = it ?: return@Observer

            exeList.adapter = ExerciseAdapter(this, exerciseList)
            spinner.visibility = View.GONE
        })

        search.setOnClickListener {
            spinner.visibility = View.VISIBLE
            exerciseViewModel.getExercisesBySearch(searchBar.text.toString())
        }

        newExe.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_new_exercise)
            dialog.setTitle("New Exercise")

            val save: Button = dialog.findViewById(R.id.saveExerciseButton) as Button
            save.setOnClickListener {
                val name = dialog.findViewById(R.id.editExerciseName) as EditText
                val desc = dialog.findViewById(R.id.editDesc) as EditText
                val tags = dialog.findViewById(R.id.editTags) as EditText

                val newName = name.text.toString()
                val newDesc = desc.text.toString()
                val newTags = ArrayList(tags.text.toString().split(","))
                val newID = exerciseViewModel.addExercise(
                    newName,
                    newDesc,
                    newTags
                )

                dialog.dismiss()
                goBack(newID)
            }

            val cancel: Button = dialog.findViewById(R.id.cancelExerciseButton) as Button
            cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun goBack(newID: String) {
        val intent = Intent()
        intent.putExtra(WorkoutTemplateActivity.EXTRA_IDENTITY, newID)
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()

    }
}