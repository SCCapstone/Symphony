/*
 * Created by Team Symphony 12/2/22, 7:32 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:32 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.History

/**
 * Adapter for dynamically populating a card_history with a passed list of Historys
 */

class HistoryAdapter (val context: Context, val data: ArrayList<History>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_history, viewGroup, false)
        v.setOnClickListener {  }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.historyTitle.text = data[i].name
        holder.historyTimestamp.text = data[i].date?.toDate().toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(exeView: View) : RecyclerView.ViewHolder(exeView) {

        var historyTitle: TextView
        var historyTimestamp: TextView

        init {
            historyTitle = exeView.findViewById(R.id.historyNameTextView)
            historyTimestamp = exeView.findViewById(R.id.historyTimestamp)
        }
    }
}