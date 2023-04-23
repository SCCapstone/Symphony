/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseRepository
import com.symphony.mrfit.data.model.Workout

/**
 * Adapter for dynamically populating a card_workout2 with a passed list of Workouts
 */

class WorkoutAdapter2 (val context: Context, val data: ArrayList<Workout>): RecyclerView.Adapter<WorkoutAdapter2.ViewHolder>() {

    private var storage: FirebaseStorage = Firebase.storage

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_workout2, viewGroup, false)
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

        // Toggle the checkbox when the card is tapped
        holder.itemView.setOnClickListener {
            holder.checkbox.isChecked = !holder.checkbox.isChecked
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(workoutView: View) : RecyclerView.ViewHolder(workoutView) {

        var exerciseImage: ImageView
        var workoutTitle: TextView
        var workoutReps: TextView
        var checkbox: CheckBox

        init {
            exerciseImage = workoutView.findViewById(R.id.workoutImage)
            workoutTitle = workoutView.findViewById(R.id.workoutNameTextView)
            workoutReps = workoutView.findViewById(R.id.workoutRepsTextView)
            checkbox = workoutView.findViewById(R.id.checkBox6)
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
            myString += "$duration "
        }
        if (distance != null) {
            myString += "$distance "
        }
        return myString
    }
}