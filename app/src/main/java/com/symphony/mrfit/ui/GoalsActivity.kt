/*
 *  Created by Team Symphony on 4/1/23, 4:47 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 4:42 PM
 */

package com.symphony.mrfit.ui

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.symphony.mrfit.R
import com.symphony.mrfit.data.exercise.GoalAdapter
import com.symphony.mrfit.data.model.Goal
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityGoalsBinding
import com.symphony.mrfit.ui.Helper.ZERO

class GoalsActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        val goalsList = binding.goalsList
        val newGoal = binding.addGoalButton
        val spinner = binding.loadingSpinner

        fun deleteGoal(goalID: String) {
            profileViewModel.deleteGoal(goalID)
        }

        spinner.visibility = View.VISIBLE

        // Get data of current User and populate the page
        profileViewModel.getNotifications()

        // Set the layout of the list of workouts presented to the user
        layoutManager = LinearLayoutManager(this)
        goalsList.layoutManager = layoutManager

        // Populate the list
        profileViewModel.getGoals()
        profileViewModel.goals.observe(this, Observer {
            Log.d(ContentValues.TAG, "Filling in username")
            val goals = it ?: return@Observer

            goalsList.adapter = GoalAdapter(this, goals, ::deleteGoal)
            spinner.visibility = View.GONE
        })

        newGoal.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_new_goal)
            dialog.setTitle("New Goal")

            val name = dialog.findViewById<EditText>(R.id.editGoalName)
            val num = dialog.findViewById<EditText>(R.id.goalNumber)
            val dropdown = dialog.findViewById<Spinner>(R.id.goalSpinner)
            val other = dialog.findViewById<EditText>(R.id.otherInput)
            val save = dialog.findViewById<Button>(R.id.saveGoalButton)
            val cancel = dialog.findViewById<Button>(R.id.cancelGoalButton)

            dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == dropdown.adapter.count - 1) {
                        other.visibility = View.VISIBLE
                    } else {
                        other.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            save.setOnClickListener {
                var newName = "New Goal"
                if (name.text.isNotEmpty())
                    newName = name.text.toString()
                var newEnd = ZERO.toDouble()
                if (num.text.isNotEmpty())
                    newEnd = num.text.toString().toDouble()
                var newType = dropdown.selectedItem.toString()
                if (newType == "Other") {
                    newType = other.text.toString()
                }

                profileViewModel.addGoal(
                    Goal(
                        newName,
                        ZERO.toDouble(),
                        newEnd,
                        newType
                    )
                )

                profileViewModel.getGoals()
                dialog.dismiss()
            }

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}