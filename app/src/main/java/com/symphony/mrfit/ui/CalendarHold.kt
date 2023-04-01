package com.symphony.mrfit.ui

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import java.time.LocalDate

internal abstract class CalendarHold(
    private val days: ArrayList<LocalDate>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams

        /*monthly view*/
        if (days.size > 15)
        /*weekly view*/
            layoutParams.height = (parent.height * 0.166666666).toInt() else
            layoutParams.height = parent.height
        /*return CalendarView(view, onItemListener, days)*/
    }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarView, position: Int) {
        val date = days[position]
        if (date == null) holder.dayOfMonth.setText("") else {
            holder.dayOfMonth.setText(date.dayOfMonth.toString())
            if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: ArrayList<LocalDate>, date: LocalDate?)
    }
}