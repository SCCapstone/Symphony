package com.symphony.mrfit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginRepository
import com.symphony.mrfit.databinding.ActivityRegisterBinding
import com.symphony.mrfit.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var repo: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = repo.currentUser
        val name = binding.profileNameTextView
        val edit = binding.editProfileButton
        val goal = binding.goalsButton
        val achievements = binding.achievementsButton
        val history = binding.historyButton
        val progress = binding.progressButton

        name.text = user?.name

        edit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}