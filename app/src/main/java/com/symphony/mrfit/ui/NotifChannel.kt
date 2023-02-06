package com.symphony.mrfit.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.symphony.mrfit.databinding.ActivityMainBinding
import java.util.*


//for scheduling notifs
class NotifChannel : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //createNotificationChannel()
        //when you click on button, it schedules a notif
        //binding.submitButton.setOnClickListener { scheduleNotification() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification()
    {
        val intent = Intent(applicationContext, Notification::class.java)
//        val title = binding.titleET.text.toString()
//        val message = binding.messageET.text.toString()
//        intent.putExtra(titleExtra, title)
//        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            notificationID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )

//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time, title, message)
    }

}