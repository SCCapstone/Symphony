/*
 *  Created by Team Symphony on 4/2/23, 9:44 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 9:32 PM
 */

package com.symphony.mrfit.ui

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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
        val confetti = binding.konfettiView

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )

        fun deleteGoal(goalID: String) {
            profileViewModel.deleteGoal(goalID)
        }

        fun editGoal(goal: Goal) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_edit_goal)
            dialog.setTitle("New Goal")
            Toast.makeText(this, "Opening goal ${goal.goalID}", Toast.LENGTH_SHORT).show()

            val name = dialog.findViewById<EditText>(R.id.editGoalName)
            val prog = dialog.findViewById<EditText>(R.id.progressGoalEditText)
            val end = dialog.findViewById<EditText>(R.id.endGoalEditText)
            val type = dialog.findViewById<TextView>(R.id.goalTypeTextView)
            val save = dialog.findViewById<Button>(R.id.saveEditGoalButton)
            val cancel = dialog.findViewById<Button>(R.id.cancelEditGoalButton)

            name.setText(goal.name)
            prog.hint = goal.progress.toString()
            end.hint = goal.endGoal.toString()
            type.text = (goal.quantifier)

            save.setOnClickListener {
                var newName = goal.name
                if (name.text.isNotEmpty())
                    newName = name.text.toString()
                var newProg = goal.progress
                if (prog.text.isNotEmpty())
                    newProg = prog.text.toString().toDouble()
                var newEnd = goal.endGoal
                if (end.text.isNotEmpty())
                    newEnd = end.text.toString().toDouble()
                profileViewModel.updateGoal(
                    Goal(
                        newName,
                        newProg,
                        newEnd,
                        goal.quantifier,
                        goal.goalID
                    )
                )

                if (newProg >= newEnd) {
                    confetti.start(party)
                    Toast.makeText(this, "Congrats on reaching your goal!", Toast.LENGTH_SHORT)
                        .show()
                }

                profileViewModel.getGoals()
                dialog.dismiss()
            }

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
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

            goalsList.adapter = GoalAdapter(this, goals, ::deleteGoal, ::editGoal)
            spinner.visibility = View.GONE
        })

        newGoal.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_new_goal)
            dialog.setTitle("New Goal")

            val name = dialog.findViewById<EditText>(R.id.newGoalName)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}