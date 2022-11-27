/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:37 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var repo: LoginRepository
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        repo = LoginRepository()
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory())[ProfileViewModel::class.java]
        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory())[LoginViewModel::class.java]

        /**
         * Declare lots of variables
         */
        val user = auth.currentUser
        val edit = binding.editProfileButton
        val goal = binding.goalsButton
        val achievements = binding.achievementsButton
        val history = binding.historyButton
        val progress = binding.progressButton
        val logout = binding.logoutButton
        val delete = binding.deleteButton
        val name = binding.nameTextView
        val age = binding.ageLayout
        val ageText = binding.ageValueTextView
        val height = binding.heightLayout
        val heightText = binding.heightValueTextView
        val weight = binding.weightLayout
        val weightText = binding.weightValueTextView
        val pfp = binding.profilePicture

        /**
         * Get data of current User and populate the page
         */
        profileViewModel.fetchCurrentUser()
        profileViewModel.loggedInUser.observe(this, Observer {
            Log.d(ContentValues.TAG, "Populating Profile screen with values from current user")
            val loggedInUser = it ?: return@Observer

            name.text = loggedInUser.name
            ageText.text = loggedInUser.age?.toString() ?: PLACEHOLDER
            heightText.text = loggedInUser.height?.toString() ?: PLACEHOLDER
            if (heightText.text != PLACEHOLDER) { heightText.text = getString(R.string.height_value, heightText.text) }
            weightText.text = loggedInUser.weight?.toString() ?: PLACEHOLDER
            if (weightText.text != PLACEHOLDER) { weightText.text = getString(R.string.weight_value, weightText.text) }
        })

        name.setOnClickListener {
            nameAlert(name)
        }

        age.setOnClickListener {
            ageAlert(age)
        }

        height.setOnClickListener {
            heightAlert(height)
        }

        weight.setOnClickListener {
            weightAlert(weight)
        }

        edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        goal.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }

        achievements.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }

        history.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }

        progress.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }

        logout.setOnClickListener {
            loginViewModel.logout(this)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // TODO: Add an "Are you sure?" popup box when delete button is pressed
        delete.setOnClickListener {
            deleteAlert(delete)
        }
    }

    private fun nameAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.name_alert_title)
        builder.setMessage(R.string.name_alert_message)

        val input = EditText(this)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString()
            profileViewModel.updateCurrentUser(new, null, null, null)
            Toast.makeText(
                applicationContext,
                "Name has been changed to $new",
                Toast.LENGTH_LONG
            ).show()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun ageAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.age_alert_title)
        builder.setMessage(R.string.age_alert_message)

        val input = EditText(this)
        input.hint = "Age"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toInt()
            profileViewModel.updateCurrentUser(null, new, null, null)
            Toast.makeText(
                applicationContext,
                "Age has been changed to $new",
                Toast.LENGTH_LONG
            ).show()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun heightAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.height_alert_title)
        builder.setMessage(R.string.height_alert_message)

        val input = EditText(this)
        input.hint = "Height"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toInt()
            profileViewModel.updateCurrentUser(null, null, new, null)
            Toast.makeText(
                applicationContext,
                "Height has been changed to $new",
                Toast.LENGTH_LONG
            ).show()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun weightAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.weight_alert_title)
        builder.setMessage(R.string.weight_alert_message)

        val input = EditText(this)
        input.hint = "Weight"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toDouble()
            profileViewModel.updateCurrentUser(null, null, null, new)
            Toast.makeText(
                applicationContext,
                "Weight has been changed to $new",
                Toast.LENGTH_LONG
            ).show()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun deleteAlert(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_alert_title)
        builder.setMessage(R.string.delete_alert_message)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            deleteAccount()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun deleteAccount() {
        loginViewModel.delete()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        private const val PLACEHOLDER = "--"
    }
}