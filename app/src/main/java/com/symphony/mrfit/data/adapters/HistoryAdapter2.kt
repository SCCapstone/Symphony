/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.History
import com.symphony.mrfit.ui.Helper.humanReadableDate
import com.symphony.mrfit.ui.Helper.humanReadableDuration

/**
 * Adapter for dynamically populating a card_history with a passed list of Histories
 */

class HistoryAdapter2(
    val context: Context,
    val data: ArrayList<History>,
    val delete: (String) -> Unit,
    val open: (History) -> Unit
) :
    RecyclerView.Adapter<HistoryAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_history2, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.historyName.text = data[i].name
        holder.historyProgress.text = buildString(i)

        holder.deleteButton.setOnClickListener {
            Log.e("Adapter", "Accessing $i of ${data[i]}")
            delete(data[i].historyID!!)
            data.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, itemCount)
        }

        holder.itemView.setOnClickListener {
            open(data[i])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(historyView: View) : RecyclerView.ViewHolder(historyView) {

        var historyName: TextView
        var historyProgress: TextView
        var deleteButton: Button

        init {
            historyName = historyView.findViewById(R.id.historyName)
            historyProgress = historyView.findViewById(R.id.historyProgress)
            deleteButton = historyView.findViewById(R.id.deleteHistoryButton)
        }
    }

    private fun buildString(i: Int): String {
        val milli = data[i].duration!!
        val time = humanReadableDuration(milli)
        val date = humanReadableDate(data[i].date!!.toDate())
        return "Exercised for $time on $date"
    }
}