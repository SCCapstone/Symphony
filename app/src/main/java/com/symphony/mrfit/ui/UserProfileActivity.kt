package com.symphony.mrfit.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

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

        /**
         * Declare variables
         */
        val user = auth.currentUser
        val edit = binding.editProfileButton
        val goal = binding.goalsButton
        val achievements = binding.achievementsButton
        val history = binding.historyButton
        val progress = binding.progressButton
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
    }

    companion object {
        private const val PLACEHOLDER = "--"
    }
}