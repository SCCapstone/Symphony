package com.symphony.mrfit.data.login

public interface LogInListener  {
    fun logInSuccess(email: String?, password: String?)
    fun logInFailure(exception: Exception?, email: String?, password: String?)
}