/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Goal
import java.text.DecimalFormat

/**
 * Adapter for dynamically populating a card_goal with a passed list of Histories
 */

class GoalAdapter(
    val context: Context,
    val data: ArrayList<Goal>,
    val delete: (String) -> Unit,
    val edit: (Goal) -> Unit
) :
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

        /**
         * TODO: Crashes if a deletion attempt is done before screen is restarted. WHY!?
         */
        holder.deleteButton.setOnClickListener {
            delete(data[i].goalID!!)
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