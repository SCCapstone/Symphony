/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for user-defined Goal objects
 */

data class Goal(
    val name: String,
    val description: String,
    val isComplete: Boolean
)
