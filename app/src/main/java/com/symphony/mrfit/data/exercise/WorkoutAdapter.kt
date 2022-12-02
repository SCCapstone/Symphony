/*
 * Created by Team Symphony 11/28/22, 6:56 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 6:56 PM
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
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_LIST
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_STRING
import com.symphony.mrfit.ui.WorkoutActivity

class WorkoutAdapter (val context:Context, val data: ArrayList<Workout>): RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_workout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.workoutTitle.text = data[i].name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutActivity::class.java)
            intent.putExtra(EXTRA_STRING,data[i].name)
            intent.putExtra(EXTRA_LIST,data[i].exercises)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(workoutView: View) : RecyclerView.ViewHolder(workoutView) {

            var workoutTitle: TextView
            var workoutDetail: TextView

            init {
                    workoutTitle = workoutView.findViewById(R.id.workoutNameTextView)
                    workoutDetail = workoutView.findViewById(R.id.workoutDescriptionTextView)
                }
        }
}