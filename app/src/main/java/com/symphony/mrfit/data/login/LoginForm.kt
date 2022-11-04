package com.symphony.mrfit.data.login

/**
 * Data validation class for the login form
 */

data class LoginForm(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
