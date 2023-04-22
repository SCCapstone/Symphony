/*
 *  Created by Team Symphony on 4/22/23, 6:21 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 6:05 AM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.ui.WorkoutRoutineActivity.Companion.EXTRA_ROUTINE
import com.symphony.mrfit.ui.WorkoutTemplateActivity
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_EXERCISE
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_LIST

/**
 * Adapter for dynamically populating a card_workout with
 * a passed list of Workouts, as well as the parent Routine's ID and workoutList
 */

class WorkoutAdapter(
    val context: Context,
    val data: ArrayList<Workout>,
    private val rID: String,
    private val rList: ArrayList<String>
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    private var storage: FirebaseStorage = Firebase.storage

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_workout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        holder.workoutTitle.text = data[i].workoutName
        holder.workoutReps.text = buildString(i)
        Glide.with(context)
            .load(
                storage.reference
                    .child(ExerciseRepository.EXERCISE_PICTURE)
                    .child(data[i].exercise!!)
            )
            .placeholder(R.drawable.glide_placeholder)
            .into(holder.exerciseImage)

        /**
         * Start the workout template, passing the parent Routine's ID and workoutList,
         * as well ass the current workout's Name and NumberOfReps
         */
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_ROUTINE, rID)
            intent.putExtra(EXTRA_IDENTITY, data[i].workoutID)
            intent.putExtra(EXTRA_EXERCISE, data[i].exercise)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_STRING, data[i].workoutName)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_DURA, data[i].duration)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_DIST, data[i].distance)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_REPS, data[i].numberOfReps)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_SETS, data[i].numberOfSets)
            intent.putExtra(EXTRA_LIST, rList)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(workoutView: View) : RecyclerView.ViewHolder(workoutView) {

        var exerciseImage: ImageView
        var workoutTitle: TextView
        var workoutReps: TextView

        init {
            exerciseImage = workoutView.findViewById(R.id.workoutImage)
            workoutTitle = workoutView.findViewById(R.id.workoutNameTextView)
            workoutReps = workoutView.findViewById(R.id.workoutRepsTextView)
        }
    }

    private fun buildString(i: Int): String {
        var myString = ""
        val reps = data[i].numberOfReps
        val sets = data[i].numberOfSets
        val distance = data[i].distance
        val duration = data[i].duration

        if (sets != null) {
            myString += if (sets == 1) {
                "$sets set "
            } else {
                "$sets sets "
            }
        }
        if (reps != null && sets != null) {
            myString += "of "
        }
        if (reps != null) {
            myString += if (reps == 1) {
                "$reps rep "
            } else {
                "$reps reps "
            }
        }
        if (myString != "" && (duration != null || distance != null)) {
            myString += "for "
        }
        if (duration != null) {
            myString += "Duration: $duration minutes "
        }
        if (distance != null) {
            myString += "Distance: $distance miles"
        }
        return myString
    }
}