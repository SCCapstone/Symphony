/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.symphony.mrfit.R
import com.symphony.mrfit.data.adapters.ExerciseAdapter
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.databinding.ActivityExerciseBinding

/**
 * Screen for the User to select an Exercise to pair with the parent Workout.
 * The user should also be able to enter a search term to narrow down possible Exercises.
 */

class ExerciseActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityExerciseBinding
    private val uri: Uri = Uri.parse(PLACEHOLDER_THUMBNAIL)
    private var thumbnail: MutableLiveData<Uri> =
        MutableLiveData(uri)

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                thumbnail.value = uri
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val imageSelection =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImg = result.data!!.data
                thumbnail.value = selectedImg!!
            }
        }

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
            newExercise()
        }
    }

    /**
     * Create a dialog for the User to create an Exercise
     */
    private fun newExercise() {
        // Create the dialog and inflate its view like an activity
        val materialDialog = MaterialAlertDialogBuilder(this)
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.popup_new_exercise, null, false)

        materialDialog.setView(dialogView)
            .setTitle("New Exercise")


        val image = dialogView.findViewById<ImageView>(R.id.editExerciseImage)
        val name = dialogView.findViewById<EditText>(R.id.editExerciseName)
        val desc = dialogView.findViewById<EditText>(R.id.editDesc)
        val tags = dialogView.findViewById<EditText>(R.id.editTags)
        val reps = dialogView.findViewById<CheckBox>(R.id.repsCheckBox)
        val sets = dialogView.findViewById<CheckBox>(R.id.setsCheckBox)
        val duration = dialogView.findViewById<CheckBox>(R.id.durationCheckBox)
        val distance = dialogView.findViewById<CheckBox>(R.id.distanceCheckBox)

        // Observe some livedata to know when the user has changed the picture
        thumbnail.observe(this, Observer {
            val thumbnail = it ?: return@Observer
            Glide.with(this)
                .load(thumbnail)
                .into(image)
        })

        // Add secondary hint text when the tags field to show User proper entry
        // Remove this hint when tags field loses focus
        tags.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                tags.hint = "At home, Cardio, etc."
                val inputMethodManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
                    tags,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
            if (!hasFocus)
                tags.hint = ""
        }

        image.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Save the new exercise to the database, name cannot be empty
        materialDialog.setPositiveButton(getString(R.string.button_save)) { dialog, _ ->
            if (name.text.isNotEmpty()) {
                val newName = name.text.toString()
                var newDesc = ""
                if (desc.text.isNotEmpty())
                    newDesc = desc.text.toString()
                var newTags = ArrayList<String>()
                if (tags.text.isNotEmpty())
                    newTags = ArrayList(tags.text.toString().split(",", ", "))
                val newExercise = Exercise(
                    name = newName,
                    description = newDesc,
                    tags = newTags,
                    repsFlag = reps.isChecked,
                    setsFlag = sets.isChecked,
                    durationFlag = duration.isChecked,
                    distanceFlag = distance.isChecked
                )
                val newID = exerciseViewModel.addExercise(
                    newExercise,
                    thumbnail.value!!
                )

                dialog.dismiss()
                goBack(newID)
            } else {
                Toast.makeText(
                    this,
                    "You must name your exercise",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        materialDialog.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        materialDialog.show()
    }

    private fun goBack(newID: String) {
        val intent = Intent()
        intent.putExtra(WorkoutTemplateActivity.EXTRA_IDENTITY, newID)
        this.setResult(Activity.RESULT_OK, intent)
        this.finish()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val PLACEHOLDER_THUMBNAIL =
            "android.resource://com.symphony.mrfit/drawable/glide_placeholder"
    }
}