/*
 *  Created by Team Symphony on 4/1/23, 4:23 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 4:23 AM
 */

package com.symphony.mrfit.ui

import android.app.Activity
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginResult
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.data.model.User
import com.symphony.mrfit.databinding.ActivityLoginBinding
import com.symphony.mrfit.ui.Helper.showSnackBar


/**
 * Screen for returning users to log in to an existing account
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: LoginActivity

    // Variables for Google and Meta Sign In
    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }

    private var database: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activity = this
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(
            this, LoginViewModelFactory()
        )[LoginViewModel::class.java]

        val email = binding.loginEmail
        val password = binding.loginPassword
        val emailLogin = binding.loginButton
        val googleLogin = binding.googleButton
        // val metaLogin = binding.metaButton
        val register = binding.toRegisterTextView
        val reset = binding.resetPasswordTextView
        val spinner = binding.loadingSpinner

        emailLogin.isEnabled = false

        /**
         * TODO: Split into MVVM model
         */

        // Configure Google Sign In
        signInClient = Identity.getSignInClient(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

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
                        loginViewModel.emailLogin(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        emailLogin.setOnClickListener {
            if (email.text!!.isNotEmpty() && password.text!!.isNotEmpty()) {
                spinner.visibility = View.VISIBLE
                loginViewModel.emailLogin(email.text.toString(), password.text.toString())
            } else {
                showSnackBar(getString(R.string.empty_login_warning), this)
            }
        }

        googleLogin.setOnClickListener {
            signIn()
            loginViewModel.googleLogin()
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        reset.setOnClickListener {
            resetAlert()
        }

        /**
         * Observe the form and update accordingly
         * TODO: Change so errors only show after an incorrect input
         */
        loginViewModel.loginForm.observe(this, Observer {
            val loginState = it ?: return@Observer

            // Disable login button until all fields are valid
            emailLogin.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })


        // Observe if the currently logged in user becomes populated
        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            spinner.visibility = View.GONE

            if (loginResult.error != null) {
                Log.d(TAG, "UI thinks login failed")
                showLoginFailed()
            } else {
                Log.d(TAG, "UI thinks login succeeded")
                gotoHomeScreen(loginResult)
            }
            setResult(Activity.RESULT_OK)
        })
    }

    /**
     * TODO: Split into MVVM model
     */
    // region Google Sign In
    private fun handleSignInResult(data: Intent?) {
        // Result returned from launching the Sign In PendingIntent
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Log.d(TAG, "signInWithCredential:attempt")
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val newUser = User(userID = user!!.uid, name = user.displayName)
                    // If this is the first time signing in a user, add them to the database
                    val new = task.result.additionalUserInfo!!.isNewUser
                    if (new) {
                        database.collection("users").document(newUser.userID).set(newUser)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error writing document", e)
                            }
                    }
                    gotoHomeScreen(LoginResult(success = newUser.name))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //val view = binding.mainLayout
                    //Snackbar.make(, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Google Sign-in failed", e)
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }
    // endregion Google Sign In

    // After a successful login, go to the home screen
    private fun gotoHomeScreen(model: LoginResult) {
        val welcome = getString(R.string.welcome)
        val user = model.success
        Toast.makeText(applicationContext, "$welcome $user", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        //Complete and destroy login activity once successful
        finish()
    }

    private fun showLoginFailed() {
        showSnackBar(getString(R.string.login_failed), this)
    }

    private fun resetAlert() {val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.label_password_reset)
        builder.setMessage(R.string.message_password_reset)

        val input = EditText(this)
        input.hint = "Email"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            loginViewModel.passwordReset(input.text.toString())
            showSnackBar(
                getString(R.string.reset_email_sent, input.text.toString()),this
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ ->
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