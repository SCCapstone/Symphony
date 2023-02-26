/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.login

/**
 * Data validation class for Firebase User Auth attempts
 */

data class LoginResult(
    val success: String? = null,
    val error: Int? = null
)
