/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:37 PM
 */

package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]
        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory()
        )[LoginViewModel::class.java]

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
        val name = binding.profileNameTextView
        val age = binding.ageValueTextView
        val height = binding.heightValueTextView
        val weight = binding.weightValueTextView
        val pfp = binding.profilePicture

        /**
         * Get data of current User and populate the page
         */
        profileViewModel.fetchCurrentUser()
        profileViewModel.loggedInUser.observe(this, Observer {
            Log.d(ContentValues.TAG, "Populating Profile screen with values from current user")
            val loggedInUser = it ?: return@Observer

            name.text = loggedInUser.name
            age.text = loggedInUser.age?.toString() ?: PLACEHOLDER
            height.text = loggedInUser.height?.toString() ?: PLACEHOLDER
            if (height.text != PLACEHOLDER) { height.text = getString(R.string.height_value, height.text) }
            weight.text = loggedInUser.weight?.toString() ?: PLACEHOLDER
            if (weight.text != PLACEHOLDER) { weight.text = getString(R.string.weight_value, weight.text) }
        })

        edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            loginViewModel.logout()
            finish()
        }

        // TODO: Add an "Are you sure?" popup box when delete button is pressed
        delete.setOnClickListener {
            loginViewModel.delete()
            finish()
        }
    }

    companion object {
        private const val PLACEHOLDER = "--"
    }
}