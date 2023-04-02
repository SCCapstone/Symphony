/*
 *  Created by Team Symphony on 4/2/23, 2:50 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 2:50 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.ExerciseRepository.Companion.EXERCISE_PICTURE
import com.symphony.mrfit.data.model.Exercise

/**
 * Adapter for dynamically populating a card_exercise with a passed list of Exercises
 */

class ExerciseAdapter2(
    val context: Context,
    val data: ArrayList<Exercise>,
    val delete: (String) -> Unit,
    val edit: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter2.ViewHolder>() {


    private var storage: FirebaseStorage = Firebase.storage

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_exercise2, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.exeTitle.text = data[i].name
        holder.exeTags.text = data[i].tags.toString()
        holder.exeDetail.text = data[i].description
        Glide.with(context)
            .load(
                storage.reference
                    .child(EXERCISE_PICTURE)
                    .child(data[i].exerciseID!!)
            )
            .placeholder(R.drawable.cactuar)
            .into(holder.exeImage)

        holder.deleteButton.setOnClickListener {
            delete(data[i].exerciseID!!)
            data.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, itemCount)
        }

        holder.itemView.setOnClickListener {
            edit(data[i])
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(exeView: View) : RecyclerView.ViewHolder(exeView) {

        var exeImage: ImageView
        var exeTitle: TextView
        var exeTags: TextView
        var exeDetail: TextView
        var deleteButton: Button

        init {
            exeImage = exeView.findViewById(R.id.exerciseImage)
            exeTitle = exeView.findViewById(R.id.exerciseNameTextView)
            exeTags = exeView.findViewById(R.id.exerciseTagsTextView)
            exeDetail = exeView.findViewById(R.id.exerciseDescriptionTextView)
            deleteButton = exeView.findViewById(R.id.deleteExerciseButton)
        }
    }
}