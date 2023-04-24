/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.Notification
import com.symphony.mrfit.data.profile.UserRepository
import com.symphony.mrfit.ui.Helper.humanReadableDateTime
import java.util.*

/**
 * Adapter for dynamically populating a card_notification with a passed list of Notifications
 */

class NotificationAdapter(
    val context: Context,
    val data: ArrayList<Notification>,
    val delete: (String) -> Unit,
    val cancel: (String) -> Unit
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    val repo = UserRepository

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_notification, viewGroup, false)
        v.setOnClickListener { }
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.notificationMessage.text = data[i].message
        val date = data[i].date!!.toDate()
        holder.notificationTimestamp.text = humanReadableDateTime(date)
        // If the Notification was from before the current date
        // allow user to delete it
        if (date.before(Date())) {
            holder.cancelButton.visibility = View.GONE
            holder.deleteButton.visibility = View.VISIBLE
        } else { // Otherwise, user can cancel it
            holder.cancelButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.GONE
        }

        holder.deleteButton.setOnClickListener {
            delete(data[i].date!!.toDate().time.toString())
            data.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, itemCount)
        }

        holder.cancelButton.setOnClickListener {
            val time = data[i].date!!.toDate().time.toString()
            cancel(time)
            Log.e("Notifications", "Cancelling alarm: $time")
            delete(time)
            data.removeAt(i)
            notifyItemRemoved(i)
            notifyItemRangeChanged(i, itemCount)
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