package com.symphony.mrfit.data.login

/**
 * Data validation class for the register form
 */

data class RegisterForm(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmError: Int? = null,
    val isDataValid: Boolean = false
)
