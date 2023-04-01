package com.symphony.mrfit.ui

import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

class Event(var name: String, var date: LocalDate, var time: LocalTime) {

    companion object {
        var eventsList = ArrayList<Event>()
        fun eventsForDate(date: LocalDate): ArrayList<Event> {
            val events = ArrayList<Event>()
            /*for events in our event list*/
            for (event in eventsList) {
                /*if event date equals event date passed in*/
                if (event.date == date) events.add(event) /*add it to events*/
            }
            return events
        }
    }
}