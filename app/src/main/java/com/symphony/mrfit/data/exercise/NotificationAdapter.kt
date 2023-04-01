/*
 *  Created by Team Symphony on 3/31/23, 11:31 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/31/23, 11:31 PM
 */

package com.symphony.mrfit.data.exercise

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Notification
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for dynamically populating a card_history with a passed list of Histories
 */

class NotificationAdapter(val context: Context, val data: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_history, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.notificationMessage.text = data[i].message
        val date = data[i].date!!.toDate()
        holder.notificationTimestamp.text = SimpleDateFormat(
            "MMMM dd, yyyy at hh:mm a",
            Locale.getDefault()
        )
            .format(date)

        // If the Notification was from before the current date
        // allow user to delete it
        if (date.before(Date())) {
            holder.cancelButton.visibility = View.GONE
            holder.deleteButton.visibility = View.VISIBLE
        } else { // Otherwise, user can cancel it
            holder.cancelButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(notifView: View) : RecyclerView.ViewHolder(notifView) {

        var notificationMessage: TextView
        var notificationTimestamp: TextView
        var cancelButton: Button
        var deleteButton: Button

        init {
            notificationMessage = notifView.findViewById(R.id.notificationMessage)
            notificationTimestamp = notifView.findViewById(R.id.notificationTimeAndDate)
            cancelButton = notifView.findViewById(R.id.cancelNotificationButton)
            deleteButton = notifView.findViewById(R.id.deleteNotifcationButton)
        }
    }
}