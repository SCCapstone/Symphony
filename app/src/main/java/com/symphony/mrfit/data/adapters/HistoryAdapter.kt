/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.ui.Helper.humanReadableDuration
import com.symphony.mrfit.ui.Helper.humanReadableShortDate
import com.symphony.mrfit.ui.WorkoutHistoryActivity
import java.util.*

/**
 * Adapter for dynamically populating a card_history with a passed list of Histories
 */

class HistoryAdapter (val context: Context, val data: ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_history, viewGroup, false)
        v.setOnClickListener {  }
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.historyTitle.text = data[i].name
        holder.historyTimestamp.text = humanReadableShortDate(data[i].date!!.toDate())
        val time = humanReadableDuration(data[i].duration!!)
        holder.historyDuration.text = "Exercised for $time"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WorkoutHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(histView: View) : RecyclerView.ViewHolder(histView) {

        var historyTitle: TextView
        var historyTimestamp: TextView
        var historyDuration: TextView

        init {
            historyTitle = histView.findViewById(R.id.historyNameTextView)
            historyTimestamp = histView.findViewById(R.id.historyTimestamp)
            historyDuration = histView.findViewById(R.id.historyDurationTextView)
        }
    }
}