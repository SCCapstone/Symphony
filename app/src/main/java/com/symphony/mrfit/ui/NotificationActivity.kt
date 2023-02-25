/*
 * Created by Team Symphony 11/28/22, 11:24 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/28/22, 11:23 PM
 */

package com.symphony.mrfit.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.databinding.ActivityNotificationBinding
import java.util.*
import java.util.Calendar

class NotificationActivity : AppCompatActivity() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private lateinit var binding: ActivityNotificationBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()
        binding.scheduleNotificationButton.setOnClickListener{scheduleNotification()}


//        schedule.setOnClickListener {
//            /**
//             * TODO: Parse the user inputs to properly title and schedule the notification
//             * Try this tutorial here:
//             *   https://www.tutorialspoint.com/how-to-set-an-android-notification-to-a-specific-date-in-the-future
//             */
//            scheduleNotification(getNotification("Example Notification"), 5000)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val notificationIntent = Intent(applicationContext, Notifications::class.java)
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()
        notificationIntent.putExtra(titleExtra, title)
        notificationIntent.putExtra(messageExtra,message)

        val pendingIntent= PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val name = "Notification Channel"
        val desc = "Description of Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID,name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled").setMessage(
                "Title: "+title +
                        "\nMessage: "+message+
                        "\nAt: "+dateFormat.format(date)+ " "+timeFormat.format(date))
            .setPositiveButton("Okay"){_,_->}
            .show()

    }
    @RequiresApi(Build.VERSION_CODES.M)
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
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun scheduleNotification(notification: Notification, delay: Int) {
//        val notificationIntent = Intent(this, Notifications::class.java)
//        val title = binding.title.text.toString()
//        val message = binding.message.tag.toString()
//        notificationIntent.putExtra(Notifications.NOTIFICATION_ID, 1)
//        notificationIntent.putExtra(Notifications.NOTIFICATION, notification)
//       val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//       )
//       val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
//       val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
//        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
//
//
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time, title, message)
//    }

//    private fun getNotification(content: String): Notification {
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, Companion.default_notification_channel_id)
//        builder.setContentTitle("Scheduled Notification")
//        builder.setContentText(content)
//        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
//        builder.setAutoCancel(true)
//        builder.setChannelId(Companion.NOTIFICATION_CHANNEL_ID)
//        return builder.build()
//    }



    /**
     * just added the GetTime fuction for scheduling the notif
     * you can comment this out if experiencing errors
     */


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val default_notification_channel_id = "default"
    }
}

