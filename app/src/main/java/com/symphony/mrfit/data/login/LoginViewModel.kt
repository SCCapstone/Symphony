package com.symphony.mrfit.data.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.symphony.mrfit.R

/**
 * Class for talking between the Login UI and the data repository
 * NOTE: View should never deal with data directly, only through LiveData objects
 */

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {

    private val _loginForm = MutableLiveData<LoginForm>()
    val loginForm: LiveData<LoginForm> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    // Tell the repository to attempt to login and return the result
    fun login(activity: android.app.Activity, email: String, password: String) {
        val result = loginRepository.login(activity, email, password)

        if (result) {
            _loginResult.value =
                LoginResult(success = loginRepository.getUsername())
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    // Update the login form after the user has input data
    fun loginDataChanged(email: String, password: String) {
        if (!isUSerNameValid(email)) {
            _loginForm.value = LoginForm(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginForm(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginForm(isDataValid = true)
        }
    }

    // TODO: Add more email validation
    private fun isUSerNameValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // TODO: Add more password validation
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}