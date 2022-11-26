/*
 * Created by Team Symphony 11/26/22, 3:06 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/26/22, 3:06 PM
 */

package com.symphony.mrfit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.symphony.mrfit.R
import com.symphony.mrfit.databinding.ActivityHomeBinding
import com.symphony.mrfit.databinding.ActivityRegisterBinding


private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfile = binding.userLayout
        val workout = binding.button2

        userProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        workout.setOnClickListener {
            Toast.makeText(
                applicationContext,
                "This has not been implemented yet",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}