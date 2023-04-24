/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

/**
 * Data class for a user's Workout History
 */

data class History(
    val name: String = "",
    val date: Timestamp? = null,
    val duration: Long? = null,
    val routine: String? = null,
    val historyID: String? = null
)
