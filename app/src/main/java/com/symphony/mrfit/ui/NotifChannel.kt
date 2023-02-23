//package com.symphony.mrfit.ui
//
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import android.media.RingtoneManager
//import android.os.Build
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.symphony.mrfit.R
//import com.symphony.mrfit.databinding.ActivityMainBinding
//import java.util.*
//
//
////for scheduling notifs
//class AlarmReceiver : NotifChannel() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        Log.d("this", "notify")
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            val intent = Intent(context, NotificationActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//
//            val builder = NotificationCompat.Builder(context, "111")
//                .setSmallIcon(R.drawable)
//                .setContentTitle("Alarm is running")
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setDefaults(NotificationCompat.DEFAULT_SOUND)
//                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .addAction(R.drawable.ic_baseline_stop_24,"Stop",pendingIntent)
//                .setContentIntent(pendingIntent)
//
//            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//            val r = RingtoneManager.getRingtone(context, notification)
//            r.play()
//
//            val notificationManagerCompat = NotificationManagerCompat.from(context)
//            notificationManagerCompat.notify(123, builder.build())
//
//        }
//
//    }
//
//}