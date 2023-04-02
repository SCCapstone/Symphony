/*
 *  Created by Team Symphony on 4/1/23, 10:04 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 9:30 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions.getExtensionVersion
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.data.profile.ProfileViewModel
import com.symphony.mrfit.data.profile.ProfileViewModelFactory
import com.symphony.mrfit.databinding.ActivityUserProfileBinding
import com.symphony.mrfit.ui.Helper.showSnackBar

/**
 * Screen for the User's profile. Data can be changed by tapping on it
 */

class UserProfileActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var repo: LoginRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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


        // Declare lots of variables
        val screen = binding.profileScreenView
        val spinner = binding.loadingSpinner
        val goal = binding.goalsButton
        val achievements = binding.achievementsButton
        val history = binding.historyButton
        val notifications = binding.notificationsButton
        val logout = binding.logoutButton
        val delete = binding.deleteButton
        val name = binding.profileNameTextView
        val age = binding.ageLayout
        val ageText = binding.ageValueTextView
        val height = binding.heightLayout
        val heightText = binding.heightValueTextView
        val weight = binding.weightLayout
        val weightText = binding.weightValueTextView
        val pfp = binding.profilePicture


        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")

                    pfp.setImageURI(uri)
                    profileViewModel.changeProfilePicture(uri)
                    Glide.with(this)
                        .load(uri).circleCrop().into(pfp)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        val imageSelection =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedImg = result.data!!.data

                    pfp.setImageURI(selectedImg)
                    profileViewModel.changeProfilePicture(selectedImg!!)
                    Glide.with(this)
                        .load(selectedImg).circleCrop().into(pfp)
                }
            }

        // Hide the screen till loading is done
        screen.visibility = View.GONE
        spinner.visibility = View.VISIBLE

        // Get data of current User and populate the page
        profileViewModel.fetchCurrentUser()
        profileViewModel.loggedInUser.observe(this, Observer {
            Log.d(TAG, "Populating Profile screen with values from current user")
            val loggedInUser = it ?: return@Observer

            Glide.with(this)
                .load(profileViewModel.getProfilePicture())
                .placeholder(com.firebase.ui.auth.R.drawable.fui_ic_anonymous_white_24dp)
                .circleCrop()
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(pfp)
            name.text = loggedInUser.name
            ageText.text = loggedInUser.age?.toString() ?: PLACEHOLDER
            heightText.text = loggedInUser.height?.toString() ?: PLACEHOLDER
            if (heightText.text != PLACEHOLDER) {
                val t1 = heightText.text.toString().toInt() / 12
                val t2 = heightText.text.toString().toInt() % 12
                heightText.text = getString(R.string.height_value, t1.toString(), t2.toString())
            }
            weightText.text = loggedInUser.weight?.toString() ?: PLACEHOLDER
            if (weightText.text != PLACEHOLDER) {
                weightText.text = getString(R.string.weight_value, weightText.text)
            }

            screen.visibility = View.VISIBLE
            spinner.visibility = View.GONE
        })

        name.setOnClickListener {
            nameAlert()
        }

        age.setOnClickListener {
            ageAlert()
        }

        height.setOnClickListener {
            heightAlert()
        }

        weight.setOnClickListener {
            weightAlert()
        }

        pfp.setOnClickListener {
            // Check if the user is on a version of android that supports to the Photo Picker
            if (isPhotoPickerAvailable()) {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                imageSelection.launch(intent)
            }
        }

        goal.setOnClickListener {
            val intent = Intent(this, GoalsActivity::class.java)
            startActivity(intent)
        }

        achievements.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }

        history.setOnClickListener {
            val intent = Intent(this, WorkoutHistoryActivity::class.java)
            startActivity(intent)
        }

        notifications.setOnClickListener {
            val intent = Intent(this, NotificationLogActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            loginViewModel.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        delete.setOnClickListener {
            deleteAlert()
        }
    }

    private fun nameAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_name)
        builder.setMessage(R.string.message_change_name)

        val input = EditText(this)
        input.hint = "Name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString()
            profileViewModel.updateCurrentUser(new, null, null, null)
            showSnackBar(
                getString(R.string.name_change, new),
                this
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun ageAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_age)
        builder.setMessage(R.string.message_change_age)

        val input = EditText(this)
        input.hint = "Age"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toInt()
            profileViewModel.updateCurrentUser(null, new, null, null)
            showSnackBar(
                getString(R.string.age_change, new),
                this
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun heightAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_height)
        builder.setMessage(R.string.message_change_height)

        val input = EditText(this)
        input.hint = "Height"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toInt()
            profileViewModel.updateCurrentUser(null, null, new, null)
            showSnackBar(
                getString(R.string.height_change, new),
                this
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun weightAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_weight)
        builder.setMessage(R.string.message_change_weight)

        val input = EditText(this)
        input.hint = "Weight"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val new = input.text.toString().toDouble()
            profileViewModel.updateCurrentUser(null, null, null, new)
            showSnackBar(
                getString(R.string.weight_change, new),
                this
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun deleteAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_delete)
        builder.setMessage(R.string.message_delete_account)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            deleteAccount()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
        }
        builder.show()
    }

    private fun deleteAccount() {
        loginViewModel.delete()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getExtensionVersion(Build.VERSION_CODES.R) >= 2
        } else {
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    companion object {
        private const val PLACEHOLDER = "--"
    }
}