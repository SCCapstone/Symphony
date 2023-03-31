//package com.symphony.mrfit.ui
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import com.symphony.mrfit.ui.MainActivity.NOTIFICATION_CHANNEL_ID
//
////publishes the notification
//class NotifChannel : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val notificationChannel = NotificationChannel(
//                NOTIFICATION_CHANNEL_ID,
//                "NOTIFICATION_CHANNEL_NAME",
//                importance
//            )
//            assert(notificationManager != null)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
//        assert(notificationManager != null)
//        notificationManager.notify(id, notification)
//    }
//
//    companion object {
//        const val NOTIFICATION_CHANNEL_ID = "10001"
//        const val default_notification_channel_id = "default"
//    }
//}

