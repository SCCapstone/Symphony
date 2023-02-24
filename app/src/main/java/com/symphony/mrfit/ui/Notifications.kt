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
import androidx.core.app.NotificationCompat
import com.symphony.mrfit.R
import com.symphony.mrfit.ui.NotificationActivity.Companion.NOTIFICATION_CHANNEL_ID

//declaring const values
const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleEX"
const val messageExtra = "messageEX"


class Notifications : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        manager.notify(notificationID,notification)


    }
}
//    companion object {
//        const val NOTIFICATION_CHANNEL_ID = "Channel1"
//        const val NOTIFICATION = "notification"
//    }
//}