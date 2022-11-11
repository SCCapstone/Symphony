/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
 */

package com.symphony.mrfit.data.login

/**
 * Data validation class for the login form
 */

data class LoginForm(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
