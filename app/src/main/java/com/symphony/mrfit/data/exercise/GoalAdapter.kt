/*
 *  Created by Team Symphony on 4/1/23, 3:10 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 3:10 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Goal
import java.text.DecimalFormat

/**
 * Adapter for dynamically populating a card_goal with a passed list of Histories
 */

class GoalAdapter(val context: Context, val data: ArrayList<Goal>, val delete: (String) -> Unit) :
    RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_goal, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.goalName.text = data[i].name
        holder.goalProgress.text = buildString(i)

        holder.deleteButton.setOnClickListener {
            delete(data[i].goalID!!)
            data.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, itemCount)
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "What happens when I press this", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(goalView: View) : RecyclerView.ViewHolder(goalView) {

        var goalName: TextView
        var goalProgress: TextView
        var deleteButton: Button

        init {
            goalName = goalView.findViewById(R.id.goalName)
            goalProgress = goalView.findViewById(R.id.goalProgress)
            deleteButton = goalView.findViewById(R.id.deleteGoalButton)
        }
    }

    private fun buildString(i: Int): String {
        val format = DecimalFormat("0.#")
        return context.getString(
            R.string.goal_format,
            format.format(data[i].progress),
            format.format(data[i].endGoal),
            data[i].quantifier
        )
    }
}