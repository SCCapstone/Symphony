/*
 * Created by Team Symphony 11/10/22, 11:39 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/10/22, 11:38 PM
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
