/*
 *  Created by Team Symphony on 4/21/23, 3:22 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/21/23, 3:22 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.WorkoutRoutine

/**
 * Adapter for dynamically populating card_routine with information read from a list of Routines
 */

class RoutineAdapter2(
    val context: Context,
    val data: ArrayList<WorkoutRoutine>,
    val update: (String, String, Int) -> Unit,
    private var selectedPos: Int
) : RecyclerView.Adapter<RoutineAdapter2.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_routine, viewGroup, false)
        if (selectedPos == -999)
            selectedPos = RecyclerView.NO_POSITION
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val colour: Int = if (selectedPos == i) {
            R.color.off_garnet
        } else {
            R.color.garnet
        }
        holder.cardBackground.background = ColorDrawable(context.getColor(colour))
        holder.routineTitle.text = data[i].name

        /**
         * Change the selected item
         */
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPos)
            selectedPos = holder.absoluteAdapterPosition
            notifyItemChanged(selectedPos)
            update(data[selectedPos].name, data[selectedPos].routineID, selectedPos)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(routineView: View) : RecyclerView.ViewHolder(routineView) {

        var routineTitle: TextView
        var routineDetail: TextView
        var cardBackground: RelativeLayout

        init {
            routineTitle = routineView.findViewById(R.id.workoutNameTextView)
            routineDetail = routineView.findViewById(R.id.workoutDescriptionTextView)
            cardBackground = routineView.findViewById(R.id.cardBackground)
        }
    }
}