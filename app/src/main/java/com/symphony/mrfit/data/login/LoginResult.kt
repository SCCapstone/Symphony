/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.login

/**
 * Data validation class for Firebase User Auth attempts
 */

data class LoginResult(
    val success: String? = null,
    val error: Int? = null
)
