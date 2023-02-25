/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

/**
 * Data class for a user's Workout History
 */

data class History(
    val name: String = "",
    val date: Timestamp? = null,
    val duration: Int? = null
)
