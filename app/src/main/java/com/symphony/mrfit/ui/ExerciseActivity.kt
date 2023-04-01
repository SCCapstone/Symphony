/*
 *  Created by Team Symphony on 3/31/23, 10:18 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/31/23, 10:12 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseAdapter
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
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_new_exercise)
            dialog.setTitle("New Exercise")

            val image = dialog.findViewById<ImageView>(R.id.editExerciseImage)
            val name = dialog.findViewById<EditText>(R.id.editExerciseName)
            val desc = dialog.findViewById<EditText>(R.id.editDesc)
            val tags = dialog.findViewById<EditText>(R.id.editTags)
            val reps = dialog.findViewById<CheckBox>(R.id.repsCheckBox)
            val sets = dialog.findViewById<CheckBox>(R.id.setsCheckBox)
            val duration = dialog.findViewById<CheckBox>(R.id.durationCheckBox)
            val distance = dialog.findViewById<CheckBox>(R.id.distanceCheckBox)
            val save = dialog.findViewById<Button>(R.id.saveExerciseButton)
            val cancel = dialog.findViewById<Button>(R.id.cancelExerciseButton)

            thumbnail.observe(this, Observer {
                val thumbnail = it ?: return@Observer

                Glide.with(this).load(thumbnail).into(image)
            })

            tags.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    tags.hint = "At home, Cardio, etc."
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
             * TODO: If a field is empty, replace it with placeholder text
             */
            save.setOnClickListener {
                val newName = name.text.toString()
                val newDesc = desc.text.toString()
                val newTags = ArrayList(tags.text.toString().split(",", ", "))
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
            }

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

    companion object {
        const val PLACEHOLDER_THUMBNAIL = "android.resource://com.symphony.mrfit/drawable/cactuar"
    }
}