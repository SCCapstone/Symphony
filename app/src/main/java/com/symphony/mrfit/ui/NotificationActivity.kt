/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.ui

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.R.style
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.symphony.mrfit.R
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityNotificationBinding
import com.symphony.mrfit.ui.Helper.EXTRA_DATE
import com.symphony.mrfit.ui.Helper.ZERO
import java.util.*

/**
 * Screen for allowing the User to schedule a notification
 */

class NotificationActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        // Check if on an Android version that needs notification permissions
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }

        // Get the date passed from the calendar and set the date dials to it
        val date = intent.getLongExtra(EXTRA_DATE, ZERO.toLong())
        if (date != ZERO.toLong()) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = date
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            binding.datePicker.updateDate(year, month, day)
        }

        createNotificationChannel()
        binding.scheduleNotificationButton.setOnClickListener {
            // Check and ask for permissions if needed
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Do not allow an empty message
                if (binding.messageET.text!!.isNotEmpty()) {
                    binding.loadingSpinner.visibility = View.VISIBLE
                    scheduleNotification()
                } else {
                    Toast.makeText(
                        this,
                        "Cannot schedule empty notification",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please enable Notifications to use this feature",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val notificationIntent = Intent(applicationContext, Notifications::class.java)
        val title = getString(R.string.app_name)
        val message = binding.messageET.text.toString()
        val time = getTime()

        notificationIntent.putExtra(titleExtra, title)
        notificationIntent.putExtra(messageExtra,message)
        notificationIntent.data = Uri.parse(time.toString())

        val pendingIntent= PendingIntent.getBroadcast(
            applicationContext,
            time.toInt(),
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        Log.e("Notifications", "Created alarm: $time")
        profileViewModel.addNotification(
            com.symphony.mrfit.data.model.Notification(
                message,
                Timestamp(Date(time))
            )
        )
        showAlert(time, message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "Description of Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    private fun showAlert(time: Long, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Message: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ ->
                val day = binding.datePicker.dayOfMonth
                val month = binding.datePicker.month
                val year = binding.datePicker.year

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)

                val intent = Intent()
                intent.putExtra(EXTRA_DATE, calendar.timeInMillis)
                this.setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
            .show()

    }

    /**
     * Parse the pickers for the selected date and time
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage(R.string.notification_permission_1)
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage(R.string.notification_permission_2)
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    var hasNotificationPermissionGranted = false
}

