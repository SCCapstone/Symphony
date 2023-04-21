/*
 *  Created by Team Symphony on 4/1/23, 3:10 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 3:09 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for user-defined Goal objects
 */

data class Goal(
    val name: String = "",
    val progress: Double = 0.0,
    val endGoal: Double = 0.0,
    val quantifier: String = "",
    val goalID: String? = null
)
