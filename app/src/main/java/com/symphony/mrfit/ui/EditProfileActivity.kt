/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:37 PM
 */

package com.symphony.mrfit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory()
        )[ProfileViewModel::class.java]

        val name = binding.editUserName
        val age = binding.editUserAge
        val height = binding.editUserHeight
        val weight = binding.editUserWeight

        /**
         * Update the user's profile with any information provided
         * If a field was empty, it is treated as null and will not be updated
         * Should probably move some of this logic into the ProfileViewModel
         */
        binding.saveChangesButton.setOnClickListener {
            var newName: String? = null
            if (name.text.isNotEmpty()) { newName = name.text.toString() }
            var newAge: Int? = null
            if (age.text.isNotEmpty()) { newAge = age.text.toString().toInt() }
            var newHeight: Int? = null
            if (height.text.isNotEmpty()) { newHeight = height.text.toString().toInt() }
            var newWeight: Double? = null
            if (weight.text.isNotEmpty()) { newWeight = weight.text.toString().toDouble() }
            profileViewModel.updateCurrentUser(newName, newAge, newHeight, newWeight)
            finish()
        }
    }
}