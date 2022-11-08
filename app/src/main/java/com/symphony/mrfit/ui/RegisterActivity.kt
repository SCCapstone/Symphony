package com.symphony.mrfit.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.symphony.mrfit.R
import com.symphony.mrfit.data.register.RegisterViewModel
import com.symphony.mrfit.data.register.RegisterViewModelFactory
import com.symphony.mrfit.databinding.ActivityRegisterBinding

/**
 * Screen for a new user to register ana ccount
 */

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.registerEmail
        val password = binding.registerPassword
        val confirm = binding.confirmPassword
        val register = binding.registerButton

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        /**
         * TODO: Change from copy/pasted login code to registration code
         */

        // TODO: Change so errors only show after an incorrect input
        // Observe the form and update accordingly
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
        })

        // Observe the result of attempting to log in
        registerViewModel.registerResult.observe(this, Observer {
            val registerResult = it ?: return@Observer

            if (registerResult.error != null) {
                Toast.makeText(
                    applicationContext,
                    "Register attempt failed",
                    Toast.LENGTH_LONG
                ).show()
                showRegisterFailed(registerResult.error)
            }
            if (registerResult.success != null) {
                updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy register activity once successful
            finish()
        })

        email.afterTextChanged {
            registerViewModel.registerDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    email.text.toString(),
                    password.text.toString()
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
            registerViewModel.register(email.text.toString(), password.text.toString())
        }

    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        // TODO : Navigate to the Home screen
        Toast.makeText(
            applicationContext,
            "$welcome [USER]",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showRegisterFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}