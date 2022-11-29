/*
 * Created by Team Symphony 11/28/22, 11:24 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 11:23 PM
 */

package com.symphony.mrfit.ui

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.symphony.mrfit.R
import com.symphony.mrfit.databinding.ActivityDebugBinding
import com.symphony.mrfit.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private lateinit var binding: ActivityNotificationBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val schedule = binding.scheduleNotificationButton

        schedule.setOnClickListener {
            /**
             * TODO: Parse the user inputs to properly title and schedule the notification
             * Try this tutorial here:
             *   https://www.tutorialspoint.com/how-to-set-an-android-notification-to-a-specific-date-in-the-future
             */
            scheduleNotification(getNotification("Example Notification"), 5000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(this, Notifications::class.java)
        notificationIntent.putExtra(Notifications.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(Notifications.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    private fun getNotification(content: String): Notification {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, Companion.default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId(Companion.NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val default_notification_channel_id = "default"
    }
}

