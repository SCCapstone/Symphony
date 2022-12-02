/*
 * Created by Team Symphony 11/28/22, 6:56 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 6:56 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Exercise

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
            Toast.makeText(context, "You have tapped ${data[i].name}", Toast.LENGTH_SHORT).show()
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