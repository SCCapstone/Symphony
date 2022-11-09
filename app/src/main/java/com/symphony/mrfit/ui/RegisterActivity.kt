package com.symphony.mrfit.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.R
import com.symphony.mrfit.data.login.LoginResult
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.databinding.ActivityRegisterBinding

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

        // Connect to the view model to process input data
        registerViewModel = ViewModelProvider(
            this, LoginViewModelFactory())[LoginViewModel::class.java]

        // TODO: Change so errors only show after an incorrect input
        // Observe the form and update accordingly
        registerViewModel.registerForm.observe(this, Observer {
            val registerState = it ?: return@Observer

            // TODO: Disable the button from the start? Or check validation in repo
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

        // Observe the result of attempting to log in
        registerViewModel.registerResult.observe(this, Observer {
            val registerResult = it ?: return@Observer

            if (registerResult.error != null) {
                Toast.makeText(
                    applicationContext,
                    "Registration failed",
                    Toast.LENGTH_LONG
                ).show()
                showRegisterFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                Toast.makeText(
                    applicationContext,
                    "Registration successful",
                    Toast.LENGTH_LONG
                ).show()
                gotoHomeScreen(registerResult)
            }
            setResult(Activity.RESULT_OK)
        })

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
                            activity,
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
                            activity,
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        register.setOnClickListener {
            registerViewModel.register(activity, email.text.toString(), password.text.toString())
        }

    }

    // After a successful login, go to the home screen
    private fun gotoHomeScreen(model: LoginResult) {
        val welcome = getString(R.string.welcome)
        val user = model.success
        // TODO : Navigate to the Home screen
        Toast.makeText(
            applicationContext,
            "$welcome $user",
            Toast.LENGTH_LONG
        ).show()

        //Complete and destroy login activity once successful
        finish()
    }

    // TODO: Learn how to read why registration failed and output relevant message
    // Example: Email address already in use or just server-side error
    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}