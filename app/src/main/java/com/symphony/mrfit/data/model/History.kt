/*
 *  Created by Team Symphony on 2/25/23, 1:42 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/25/23, 1:42 AM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

/**
 * Data class for a user's Workout History
 */

data class History(
    val name: String = "",
    val date: Timestamp? = null,
    val duration: Long? = null
)
