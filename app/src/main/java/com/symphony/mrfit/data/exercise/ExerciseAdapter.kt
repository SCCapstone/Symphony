/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.exercise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * Adapter for dynamically populating a card_exercise with a passed list of Exercises
 */

class ExerciseAdapter (val context:Context, val data: ArrayList<Exercise>): RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_exercise, viewGroup, false)
        v.setOnClickListener {  }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.exeTitle.text = data[i].name
        holder.exeDetail.text = data[i].description

        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.putExtra(EXTRA_IDENTITY, data[i].exerciseID)
            context as Activity
            context.setResult(Activity.RESULT_OK, intent)
            context.finish()

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(exeView: View) : RecyclerView.ViewHolder(exeView) {

            var exeImage: ImageView
            var exeTitle: TextView
            var exeDetail: TextView

            init {
                    exeImage = exeView.findViewById(R.id.exerciseImage)
                    exeTitle = exeView.findViewById(R.id.exerciseNameTextView)
                    exeDetail = exeView.findViewById(R.id.exerciseDescriptionTextView)
                }
        }
}