/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginResult
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.databinding.ActivityRegisterBinding
import com.symphony.mrfit.ui.Helper.showSnackBar

/**
 * Screen for a new user to register an account
 */

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: LoginViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var activity: RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        val email = binding.registerEmail
        val password = binding.registerPassword
        val confirm = binding.confirmPassword
        val register = binding.registerButton
        val login = binding.toLoginTextView
        val spinner = binding.loadingSpinner

        register.isEnabled = false

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                password.text.toString(),
                confirm.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    confirm.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        confirm.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    email.text.toString(),
                    password.text.toString(),
                    confirm.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        register.setOnClickListener {
            spinner.visibility = View.VISIBLE
            registerViewModel.register(email.text.toString(), password.text.toString())
        }

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        /**
         * Connect to the view model to process input data
         */
        registerViewModel = ViewModelProvider(
            this, LoginViewModelFactory())[LoginViewModel::class.java]

        /**
         * Observe the form and update accordingly
         * TODO: Change so errors only show after an incorrect input
         */
        registerViewModel.registerForm.observe(this, Observer {
            val registerState = it ?: return@Observer

            // Disable register button until all fields are valid
            register.isEnabled = registerState.isDataValid

            if (registerState.emailError != null) {
                email.error = getString(registerState.emailError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
            if (registerState.confirmError != null) {
                confirm.error = getString(registerState.confirmError)
            }
        })


        // Observe if the currently logged in user becomes populated
        registerViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            spinner.visibility = View.GONE

            if (loginResult.error != null) {
                Log.d(ContentValues.TAG, "UI thinks registration failed")
                showRegisterFailed()
            } else {
                Log.d(ContentValues.TAG, "UI thinks registration succeeded")
                gotoHomeScreen(loginResult)
            }
            setResult(Activity.RESULT_OK)
        })
    }

    /**
     * After a successful login, go to the home screen
     */
    private fun gotoHomeScreen(model: LoginResult) {
        val welcome = getString(R.string.welcome)
        val user = model.success
        Toast.makeText(applicationContext,"$welcome $user", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        //Complete and destroy login activity once successful
        finish()
    }

    private fun showRegisterFailed() {
        showSnackBar(getString(R.string.registration_failed), this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}