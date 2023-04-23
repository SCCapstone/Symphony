/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.data.adapters

import android.app.Activity
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
import com.symphony.mrfit.data.exercise.ExerciseRepository.Companion.EXERCISE_PICTURE
import com.symphony.mrfit.data.model.Exercise
import com.symphony.mrfit.ui.WorkoutTemplateActivity.Companion.EXTRA_IDENTITY

/**
 * Adapter for dynamically populating a card_exercise with a passed list of Exercises
 */

class ExerciseAdapter (val context:Context, val data: ArrayList<Exercise>): RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {


    private var storage: FirebaseStorage = Firebase.storage

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_exercise, viewGroup, false)
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
            .placeholder(R.drawable.glide_placeholder)
            .into(holder.exeImage)

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
        var exeTags: TextView
        var exeDetail: TextView

            init {
                exeImage = exeView.findViewById(R.id.exerciseImage)
                exeTitle = exeView.findViewById(R.id.exerciseNameTextView)
                exeTags = exeView.findViewById(R.id.exerciseTagsTextView)
                exeDetail = exeView.findViewById(R.id.exerciseDescriptionTextView)
                }
        }
}