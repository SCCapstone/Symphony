package com.symphony.mrfit.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.symphony.mrfit.R
import com.symphony.mrfit.ui.CalendarUtils.formattedDate
import java.time.LocalTime

class EventEdit : AppCompatActivity() {
    private var eventName: EditText? = null
    private var eventDate: TextView? = null
    private var eventTime: TextView? = null
    private var time: LocalTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        initWidgets()
        time = LocalTime.now()
        eventDate!!.text =
            "Date: " + formattedDate(CalendarUtils.selectedDate!!)
        /*eventTimeTV!!.text = "Time: " + formattedTime()*/
    }

    private fun initWidgets() {
        eventName = findViewById(R.id.eventName)
        eventDate = findViewById(R.id.eventDate)
        eventTime = findViewById(R.id.eventTime)
    }

    fun saveEventAction(view: View?) {
        val eventName = eventName!!.text.toString()
        val newEvent = Event(eventName, CalendarUtils.selectedDate!!, time!!)
        Event.eventsList.add(newEvent)
        finish()
    }
}