package com.symphony.mrfit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.symphony.mrfit.databinding.ActivityDebugBinding
import com.symphony.mrfit.ui.LoginActivity
import com.symphony.mrfit.ui.RegisterActivity
import com.symphony.mrfit.ui.UserProfileActivity

/**
 * Menu for instantly navigating to any page for debug and testing
 * To add a new link, make a button in the layout, connect to the binding
 * and attach a setOnClickListener. Copy/Paste existing examples if needed.
 */

class DebugActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = binding.debugLogin
        val register = binding.debugRegister
        val profile = binding.debugUserProfile

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}