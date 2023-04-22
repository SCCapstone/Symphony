/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.History
import java.text.SimpleDateFormat
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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        holder.historyTimestamp.text = dateFormat.format(data[i].date!!.toDate())
        val seconds = (data[i].duration?.div(1000))?.mod(60)
        val minutes = (data[i].duration?.div(1000))?.div(60)
        holder.historyDuration.text = "Exercised for $minutes:$seconds"
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