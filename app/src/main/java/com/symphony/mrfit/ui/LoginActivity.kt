/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.databinding.ActivityLoginBinding


/**
 * Screen for returning users to log in to an existing account
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: LoginActivity
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activity = this
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.loginEmail
        val password = binding.loginPassword
        val emailLogin = binding.loginButton
        val googleLogin = binding.googleButton
        val register = binding.toRegisterTextView
        val reset = binding.resetPasswordTextView

        /**
         * Connect to the view model to process input data
         */
        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory())[LoginViewModel::class.java]

        /**
         * Observe the form and update accordingly
         * TODO: Change so errors only show after an incorrect input
         */
        loginViewModel.loginForm.observe(this, Observer {
            val loginState = it ?: return@Observer

            // TODO: Disable the button from the start? Or check validation in repo
            // Disable login button until all fields are valid
            emailLogin.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        /*
        // Observe the result of attempting to log in
        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                gotoHomeScreen(loginResult)
            }
            setResult(Activity.RESULT_OK)
        })
         */

        /**
         * Observe if the currently logged in user becomes populated
         */
        loginViewModel.user.observe(this, Observer {
            val user = it ?: return@Observer

            if (user.userID == "ERROR" && user.name != null) {
                Log.d(ContentValues.TAG, "UI things login failed")
                showLoginFailed(user.name!!)
            } else if (user.name != null) {
                Log.d(ContentValues.TAG, "UI thinks login succeeded")
                gotoHomeScreen(user)
            }
            setResult(Activity.RESULT_OK)
        })

        /**
         * Google Sign In client
         */
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.google_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, 2,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(ContentValues.TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(ContentValues.TAG, e.localizedMessage)
            }


        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            activity,
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        emailLogin.setOnClickListener {
            loginViewModel.login(activity, email.text.toString(), password.text.toString())
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        reset.setOnClickListener {
            resetAlert(reset)
        }
    }

    /**
     * After a successful login, go to the home screen
     */
    private fun gotoHomeScreen(model: User) {
        val welcome = getString(R.string.welcome)
        val user = model.name
        // TODO : Navigate to the Home screen
        Toast.makeText(
            applicationContext,
            "$welcome $user",
            Toast.LENGTH_LONG
        ).show()

        //Complete and destroy login activity once successful
        finish()
    }

    /**
     * TODO: Learn how to read why login failed and output relevant message
     * Example: Password was incorrect or no account that matched a given email
     */
    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun resetAlert(view: View) {val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.reset_alert_title)
        builder.setMessage(R.string.reset_alert_message)

        val input = EditText(this)
        input.hint = "Email"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            loginViewModel.passwordReset(input.text.toString())
            Toast.makeText(
                applicationContext,
                getString(R.string.reset_email_sent, input.text.toString()),
                Toast.LENGTH_LONG
            ).show()
            Toast.makeText(applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}