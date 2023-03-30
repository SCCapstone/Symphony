/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 4:24 PM
 */

package com.symphony.mrfit.ui

import android.app.AlarmManager
import android.app.AlertDialog
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
        val notifName = binding.title
        val notifDesc = binding.message

        schedule.setOnClickListener {
            /**
             * TODO: Parse the user inputs to properly title and schedule the notification
             * Try this tutorial here:
             *   https://www.tutorialspoint.com/how-to-set-an-android-notification-to-a-specific-date-in-the-future
             */
            scheduleNotification(getNotification(notifName.text.toString(), notifDesc.text.toString()), 5000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(this, Notifications::class.java)
        val title = binding.title.text.toString()
        val message = binding.message.tag.toString()
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

        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun getNotification(title: String, content: String): Notification {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, Companion.default_notification_channel_id)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId(Companion.NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        /**
         * this code should be a notif for when it is scheduled but it is erroring
         * commenting it out for now
         */
//        AlertDialog.Builder(context:this)
//            .setTitle("Notification Scheduled")
//            .setMessage(
//            "Title: " + title +
//                    "\nMessage: " + message +
//                    "\nAt: " +dateFormat.format(date) + " " + timeFormat.format((date))
//                .setPositiveButton(int:"Okay"){_,_->}
//        .show()
    }

    /**
     * just added the GetTime fuction for scheduling the notif
     * you can comment this out if experiencing errors
     */
    private fun getTime() : Long
    {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val default_notification_channel_id = "default"
    }
}

