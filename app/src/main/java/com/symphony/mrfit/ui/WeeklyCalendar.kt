package com.symphony.mrfit.ui

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import java.time.LocalDate


class WeeklyCalendar{

    /*goes to previous week on weekly view*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.minusWeeks(1)
        //setWeekView()
    }

    /*goes to next week on weekly view*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun nextWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.plusWeeks(1)
        //setWeekView()
    }

    /*on click, it goes to selected date*/
    fun onItemClick(position: Int, date: LocalDate) {
        CalendarUtils.selectedDate = date
        //setWeekView()
    }

    /*sets event*/
    private fun setEvent() {


    }

    /*new event*/
    fun newEvent(view: View?) {

    }
}