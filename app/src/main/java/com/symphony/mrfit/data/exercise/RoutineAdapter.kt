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
import com.symphony.mrfit.data.model.WorkoutRoutine
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_LIST
import com.symphony.mrfit.ui.RoutineSelectionActivity.Companion.EXTRA_STRING
import com.symphony.mrfit.ui.WorkoutRoutineActivity
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * Adapter for dynamically populating card_routine with information read from a list of Routines
 */

class RoutineAdapter (val context:Context, val data: ArrayList<WorkoutRoutine>): RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_routine, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.routineTitle.text = data[i].name

        /**
         * Start the next activity, passing it the Routine's ID, Name, and workoutList
         */
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutRoutineActivity::class.java)
            intent.putExtra(EXTRA_IDENTITY,data[i].routineID)
            intent.putExtra(EXTRA_STRING,data[i].name)
            intent.putExtra(EXTRA_LIST,data[i].workoutList)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(routineView: View) : RecyclerView.ViewHolder(routineView) {

            var routineTitle: TextView
            var routineDetail: TextView

            init {
                    routineTitle = routineView.findViewById(R.id.workoutNameTextView)
                    routineDetail = routineView.findViewById(R.id.workoutDescriptionTextView)
                }
        }
}