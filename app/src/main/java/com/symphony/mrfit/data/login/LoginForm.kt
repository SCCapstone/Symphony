/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
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
