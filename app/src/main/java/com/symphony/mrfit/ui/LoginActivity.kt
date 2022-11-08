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
import com.symphony.mrfit.data.login.LoginResult
import com.symphony.mrfit.data.login.LoginViewModel
import com.symphony.mrfit.data.login.LoginViewModelFactory
import com.symphony.mrfit.databinding.ActivityLoginBinding

/**
 * Screen for returning users to log in to an existing account
 */

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.loginEmail
        val password = binding.loginPassword
        val login = binding.loginButton

        // Connect to the view model to process input data
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        // TODO: Change so errors only show after an incorrect input
        // Observe the form and update accordingly
        loginViewModel.loginForm.observe(this, Observer {
            val loginState = it ?: return@Observer

            // Disable login button until all fields are valid
            login.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        // Observe the result of attempting to log in
        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                Toast.makeText(
                    applicationContext,
                    "Login attempt failed",
                    Toast.LENGTH_LONG
                ).show()
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                gotoHomeScreen(loginResult)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

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
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        login.setOnClickListener {
            loginViewModel.login(email.text.toString(), password.text.toString())
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
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


}/**
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