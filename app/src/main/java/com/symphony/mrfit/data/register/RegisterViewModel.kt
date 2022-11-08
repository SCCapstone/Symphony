package com.symphony.mrfit.data.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.symphony.mrfit.R

class RegisterViewModel: ViewModel() {

    private val _registerForm = MutableLiveData<RegisterForm>()
    val registerForm: LiveData<RegisterForm> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(username: String, password: String) {
        // TODO: Add Firebase Autherization
        _registerResult.value = RegisterResult(success = true)
    }

    fun registerDataChanged(email: String, password: String) {
        if (!isUSerNameValid(email)) {
            _registerForm.value = RegisterForm(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterForm(passwordError = R.string.invalid_password)
        } else {
            _registerForm.value = RegisterForm(isDataValid = true)
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