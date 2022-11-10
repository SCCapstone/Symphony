package com.symphony.mrfit.data.login

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.symphony.mrfit.R
import com.symphony.mrfit.data.model.LoggedInUser
import com.symphony.mrfit.data.model.User
import kotlinx.coroutines.launch

/**
 * Class for talking between the Login UI and the data repository
 * NOTE: View should never deal with data directly, only through LiveData objects
 */

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {

    private val _loginForm = MutableLiveData<LoginForm>()
    val loginForm: LiveData<LoginForm> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _registerForm = MutableLiveData<RegisterForm>()
    val registerForm: LiveData<RegisterForm> = _registerForm

    private val _registerResult = MutableLiveData<LoginResult>()
    val registerResult: LiveData<LoginResult> = _registerResult

    private val _loggedInUser = MutableLiveData<User>()
    val user: LiveData<User> = _loggedInUser

    /**
     * Tell the repository to attempt to login to an existing account and return the result
     */
    fun login(activity: android.app.Activity, email: String, password: String) {
        viewModelScope.launch { loginRepository.login(activity, email, password, _loggedInUser) }
        Log.d(ContentValues.TAG, "doneWithLoginAttempt")
    }

    /**
     * Tell the repository to attempt to register a new account and return the result
     */
    fun register(activity: android.app.Activity, email: String, password: String) {
        viewModelScope.launch { loginRepository.register(activity, email, password, _loggedInUser) }
        Log.d(ContentValues.TAG, "doneWithRegisterAttempt")
    }

    /**
     * Update the login form after the user has input data
     */
    fun loginDataChanged(email: String, password: String) {
        if (!isUSerNameValid(email)) {
            _loginForm.value = LoginForm(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginForm(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginForm(isDataValid = true)
        }
    }

    /**
     * Update the registration form after the user has input data
     */
    fun registerDataChanged(email: String, password: String, confirm: String) {
        if (!isUSerNameValid(email)) {
            _registerForm.value = RegisterForm(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterForm(passwordError = R.string.invalid_password)
        } else if (!isConfirmValid(password, confirm)) {
            _registerForm.value = RegisterForm(confirmError = R.string.invalid_confirm)
        } else {
            _registerForm.value = RegisterForm(isDataValid = true)
        }
    }

    /**
     * TODO: Add more email validation
     */
    private fun isUSerNameValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    /**
     * TODO: Add more password validation
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
    }
}