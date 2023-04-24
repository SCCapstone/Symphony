/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.exercise

/**
 * Data class for checking if a routine has been successfully updated
 */

data class RoutineListener(
    val success: String? = null,
    val error: Int? = null
)
