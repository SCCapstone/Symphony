/*
 *  Created by Team Symphony on 4/20/23, 7:03 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/20/23, 7:03 PM
 */

package com.symphony.mrfit.ui

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
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseAdapter2
import com.symphony.mrfit.data.exercise.ExerciseViewModel
import com.symphony.mrfit.data.exercise.ExerciseViewModelFactory
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.databinding.ActivityCustomExercisesBinding

/**
 * Screen for the User to manage their custom Exercises
 */

class CustomExercisesActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: ActivityCustomExercisesBinding
    private val storage = Firebase.storage
    private val uri: Uri = Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomExercisesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModelFactory()
        )[ExerciseViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val exerciseRecycler = binding.exercisesList
        val newExeButton = binding.addExerciseButton
        val spinner = binding.loadingSpinner

        spinner.visibility = View.VISIBLE

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        exerciseRecycler.layoutManager = layoutManager

        //Get data of current User and populate the page
        exerciseViewModel.getExercisesByUser()
        exerciseViewModel.exerciseList.observe(this, Observer {
            val exerciseList = it ?: return@Observer

            exerciseRecycler.adapter =
                ExerciseAdapter2(this, exerciseList, ::deleteExercise, ::editExercise)
            spinner.visibility = View.GONE
        })

        newExeButton.setOnClickListener {
            it.clearFocus()
            newExercise()
        }
    }

    /**
     * Remove the associated Exercise from the database
     */
    private fun deleteExercise(exeID: String) {
        exerciseViewModel.deleteExercise(exeID)
        exerciseViewModel.getExercisesByUser()
    }

    /**
     * Create a dialog for the User to create a new Exercise
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

        // Observe some livedata to know when the user has selected an image
        thumbnail.observe(this, Observer {
            val thumbnail = it ?: return@Observer

            Glide.with(this)
                .load(thumbnail)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(image)
        })

        /**
         * Add secondary hint text when the tags field to show User proper entry
         * Remove this hint when tags field loses focus
         */
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

        /**
         * Make a new Exercise and save it to the database
         * Name cannot be empty
         */
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
                exerciseViewModel.addExercise(
                    newExercise,
                    thumbnail.value!!
                )

                exerciseViewModel.getExercisesByUser()
                thumbnail.value = Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
                dialog.dismiss()
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

    /**
     * Create a dialog for the User to edit an Exercise
     * Should be passed to the recycler's adapter
     */
    private fun editExercise(exercise: Exercise) {
        // Create the dialog and inflate its view like an activity
        val materialDialog = MaterialAlertDialogBuilder(this)
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.popup_new_exercise, null, false)

        materialDialog.setView(dialogView)
            .setTitle("Edit Exercise")


        val image = dialogView.findViewById<ImageView>(R.id.editExerciseImage)
        val name = dialogView.findViewById<EditText>(R.id.editExerciseName)
        val desc = dialogView.findViewById<EditText>(R.id.editDesc)
        val tags = dialogView.findViewById<EditText>(R.id.editTags)
        val reps = dialogView.findViewById<CheckBox>(R.id.repsCheckBox)
        val sets = dialogView.findViewById<CheckBox>(R.id.setsCheckBox)
        val duration = dialogView.findViewById<CheckBox>(R.id.durationCheckBox)
        val distance = dialogView.findViewById<CheckBox>(R.id.distanceCheckBox)
        var loadDefault = true

        // Populate the dialog with info from the Exercise
        name.setText(exercise.name)
        desc.setText(exercise.description)
        tags.setText(exercise.tags.toString().drop(1).dropLast(1))
        reps.isChecked = exercise.repsFlag
        sets.isChecked = exercise.setsFlag
        duration.isChecked = exercise.durationFlag
        distance.isChecked = exercise.distanceFlag

        // Observe some livedata to know when the user has changed the picture
        thumbnail.observe(this, Observer {
            val thumbnail = it ?: return@Observer

            if (loadDefault) {
                Glide.with(this)
                    .load(exerciseViewModel.getExerciseImage(exercise.exerciseID!!))
                    .into(image)
                loadDefault = false
            } else {
                Glide.with(this)
                    .load(thumbnail)
                    .into(image)
            }
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

        // Save any changes to the Exercise
        // If a field is blank, the old data persists
        materialDialog.setPositiveButton(getString(R.string.button_save)) { dialog, _ ->
            var newName = exercise.name
            if (name.text.isNotEmpty())
                newName = name.text.toString()
            var newDesc = exercise.description
            if (desc.text.isNotEmpty())
                newDesc = desc.text.toString()
            var newTags = exercise.tags
            if (tags.text.isNotEmpty())
                newTags = ArrayList(tags.text.toString().split(",", ", "))
            val newExercise = Exercise(
                name = newName,
                description = newDesc,
                tags = newTags,
                repsFlag = reps.isChecked,
                setsFlag = sets.isChecked,
                durationFlag = duration.isChecked,
                distanceFlag = distance.isChecked,
                ownerID = exercise.ownerID,
                exerciseID = exercise.exerciseID
            )

            exerciseViewModel.updateExercise(newExercise)
            exerciseViewModel.changeExerciseImage(exercise.exerciseID!!, thumbnail.value!!)
            exerciseViewModel.getExercisesByUser()
            thumbnail.value = Uri.parse(ExerciseActivity.PLACEHOLDER_THUMBNAIL)
            dialog.dismiss()
        }

        materialDialog.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        materialDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}