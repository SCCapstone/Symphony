/*
 *  Created by Team Symphony on 4/1/23, 5:08 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 5:08 AM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Goal
import java.util.*

/**
 * Adapter for dynamically populating a card_goal with a passed list of Histories
 */

class GoalAdapter(val context: Context, val data: ArrayList<Goal>) :
    RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_goal, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.goalTitle.text = data[i].name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(histView: View) : RecyclerView.ViewHolder(histView) {

        var goalTitle: TextView
        var goalTimestamp: TextView
        var goalDuration: TextView

        init {
            goalTitle = histView.findViewById(R.id.goalNameTextView)
            goalTimestamp = histView.findViewById(R.id.goalTimestamp)
            goalDuration = histView.findViewById(R.id.goalDurationTextView)
        }
    }
}