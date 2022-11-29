package com.symphony.mrfit.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.symphony.mrfit.R

//declaring const values
const val notifications = 1
const val channel = "channel1"
const val title = "title"
const val message = "message"

class Notifications : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        //takes in context and channel
        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(title))
            .setContentText(intent.getStringExtra(message))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notifications, notification)
    }

}