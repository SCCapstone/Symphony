/*
 *  Created by Team Symphony on 4/1/23, 10:04 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/1/23, 10:04 PM
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
