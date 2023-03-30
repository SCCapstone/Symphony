/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.symphony.mrfit.ui.NotificationActivity.Companion.NOTIFICATION_CHANNEL_ID

//declaring const values
const val notifications = 1
const val channel = "channel1"
const val title = "title"
const val message = "message"

class Notifications : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification? = intent.getParcelableExtra(Companion.NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(Companion.NOTIFICATION_ID, 0)
        assert(notificationManager != null)
        notificationManager.notify(id, notification)
    }

    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
    }
}