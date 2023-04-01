package com.symphony.mrfit.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import java.time.LocalDate

class CalendarView private constructor(
    itemView: View,
    onItemListener: CalendarHold.OnItemListener,
    days: ArrayList<LocalDate>
    )

    : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView
    private val onItemListener: CalendarHold.OnItemListener

    init {
        parentView = itemView.findViewById<View>(R.id.parentView)
        dayOfMonth = itemView.findViewById<TextView>(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        this.days = days
    }

    /*this errors when uncommented*/
    override fun onClick(view: View) {
        /*onItemListener.onItemClick(, days)*/
    }
}