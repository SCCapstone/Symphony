/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.login

/**
 * Data validation class for the registration form
 */

data class RegisterForm(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmError: Int? = null,
    val isDataValid: Boolean = false
)
