/*
 *  Created by Team Symphony on 4/2/23, 1:04 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 12:56 AM
 */

package com.symphony.mrfit.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.symphony.mrfit.R

//declaring const values
const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleEX"
const val messageExtra = "messageEX"


class Notifications : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.mip_logo)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        manager.notify(notificationID,notification)


    }

}