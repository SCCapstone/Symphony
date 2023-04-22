/*
 *  Created by Team Symphony on 4/22/23, 5:12 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/22/23, 4:43 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.symphony.mrfit.R
import com.symphony.mrfit.data.adapters.GoalAdapter
import com.symphony.mrfit.data.model.Goal
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityGoalsBinding
import com.symphony.mrfit.ui.Helper.ZERO
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * Screen for the User to manage their Goals
 */

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
        val goalTypes = resources.getStringArray(R.array.goal_types)

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )

        /**
         * Create a dialog for the user to make a new Goal
         */
        fun newGoal() {
            // Create the dialog and inflate its view like an activity
            val materialDialog = MaterialAlertDialogBuilder(this)
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.popup_new_goal, null, false)

            materialDialog.setView(dialogView)
                .setTitle("New Goal")

            val name = dialogView.findViewById<EditText>(R.id.newGoalName)
            val num = dialogView.findViewById<EditText>(R.id.goalNumber)
            val dropdown = dialogView.findViewById<Spinner>(R.id.goalSpinner)
            val other = dialogView.findViewById<EditText>(R.id.otherInput)

            // When "Other", the last option is selected in the spinner
            // make the associated EditText visible
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

            // Save the new Goal, if any fields are empty use placeholders
            materialDialog.setPositiveButton("Save") { dialog, _ ->
                var newName = "New Goal"
                if (name.text.isNotEmpty())
                    newName = name.text.toString()
                var newEnd = ZERO.toDouble()
                if (num.text.isNotEmpty())
                    newEnd = num.text.toString().toDouble()
                var newType = dropdown.selectedItem.toString()
                if (newType == "Other") {
                    if (other.text.isNotEmpty()) {
                        newType = other.text.toString()
                    }
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

            materialDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            materialDialog.show()
        }

        /**
         * Create a dialog for the user to edit an existing Goal
         * Should be passed to the recycler's adapter
         */
        fun editGoal(goal: Goal) {
            // Create the dialog and inflate its view like an activity
            val format = DecimalFormat("0.#")
            val materialDialog = MaterialAlertDialogBuilder(this)
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.popup_edit_goal, null, false)

            materialDialog.setView(dialogView)

            val name = dialogView.findViewById<EditText>(R.id.editGoalName)
            val prog = dialogView.findViewById<EditText>(R.id.progressGoalEditText)
            val end = dialogView.findViewById<EditText>(R.id.endGoalEditText)
            val dropdown = dialogView.findViewById<Spinner>(R.id.editGoalSpinner)
            val other = dialogView.findViewById<EditText>(R.id.editGoalOther)

            // Populate the dialog with appropriate info from the Goal
            name.setText(goal.name)
            prog.hint = format.format(goal.progress)
            end.hint = format.format(goal.endGoal)
            val i = goalTypes.indexOf(goal.quantifier)
            if (i >= 0) {
                dropdown.setSelection(i)
            } else {
                dropdown.setSelection(goalTypes.size - 1)
                other.visibility = View.VISIBLE
            }
            other.hint = goal.quantifier

            // When "Other", the last option is selected in the spinner
            // make the associated EditText visible
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

            /**
             * Save the edited goal
             * If a field is empty, the old value persists
             */
            materialDialog.setPositiveButton(getString(R.string.button_save)) { dialog, _ ->
                var newName = goal.name
                if (name.text.isNotEmpty())
                    newName = name.text.toString()
                var newProg = goal.progress
                if (prog.text.isNotEmpty())
                    newProg = prog.text.toString().toDouble()
                var newEnd = goal.endGoal
                if (end.text.isNotEmpty())
                    newEnd = end.text.toString().toDouble()
                var newType = dropdown.selectedItem.toString()
                if (newType == "Other") {
                    newType = if (other.text.isNotEmpty()) {
                        other.text.toString()
                    } else {
                        goal.quantifier
                    }
                }

                profileViewModel.updateGoal(
                    Goal(
                        newName,
                        newProg,
                        newEnd,
                        newType,
                        goal.goalID
                    )
                )

                // User finished their goal, grats
                if (newProg >= newEnd) {
                    confetti.start(party)
                    Toast.makeText(this, "Congrats on reaching your goal!", Toast.LENGTH_SHORT)
                        .show()
                }

                profileViewModel.getGoals()
                dialog.dismiss()
            }

            materialDialog.setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            materialDialog.show()
        }

        /**
         * Remove the associated Goal from the database
         * Should be passed to the recycler's adapter
         */
        fun deleteGoal(goalID: String) {
            profileViewModel.deleteGoal(goalID)
            profileViewModel.getGoals()
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
            newGoal()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}