/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:15 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Workout
import com.symphony.mrfit.ui.WorkoutTemplateActivity
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_LIST

/**
 * Adapter for dynamically populating a card_workout with
 * a passed list of Workouts, as well as the parent Routine's ID and workoutList
 */

class WorkoutAdapter (val context: Context, val data: ArrayList<Workout>, val rID: String, val rList: ArrayList<String>): RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_workout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {

        holder.workoutTitle.text = data[i].workoutName
        holder.workoutReps.text = "${data[i].numberOfReps.toString()} Reps"

        /**
         * Start the workout template, passing the parent Routine's ID and workoutList,
         * as well ass the current workout's Name and NumberOfReps
         */
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutTemplateActivity::class.java)
            intent.putExtra(EXTRA_IDENTITY, rID)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_STRING,data[i].workoutName)
            intent.putExtra(WorkoutTemplateActivity.EXTRA_REPS,data[i].numberOfReps.toString())
            intent.putExtra(EXTRA_LIST, rList)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(workoutView: View) : RecyclerView.ViewHolder(workoutView) {

        var workoutTitle: TextView
        var workoutReps: TextView

        init {
            workoutTitle = workoutView.findViewById(R.id.workoutNameTextView)
            workoutReps = workoutView.findViewById(R.id.workoutRepsTextView)
        }
    }
}