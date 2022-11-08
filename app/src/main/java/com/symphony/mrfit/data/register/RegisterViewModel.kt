package com.symphony.mrfit.data.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.symphony.mrfit.R

/**
 * Class for talking between the Registration UI and the data repository
 * NOTE: View should never deal with data directly, only through LiveData objects
 */

class RegisterViewModel(private val registerRepository: RegisterRepository): ViewModel() {

    private val _registerForm = MutableLiveData<RegisterForm>()
    val registerForm: LiveData<RegisterForm> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    // Tell the repository to attempt to login and return the result
    fun register(activity: android.app.Activity, email: String, password: String) {
        val result = registerRepository.register(activity, email, password)

        if (result) {
            _registerResult.value =
                RegisterResult(success = registerRepository.getUsername())
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    // Update the registration form after the user has input data
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

    private fun isConfirmValid(password: String, confirm: String): Boolean {
        return password == confirm
    }
}