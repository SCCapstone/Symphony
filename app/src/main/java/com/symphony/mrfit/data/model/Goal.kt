/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/1/22, 6:17 PM
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
