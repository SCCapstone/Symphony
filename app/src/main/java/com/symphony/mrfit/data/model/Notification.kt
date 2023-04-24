/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

data class Notification(
    val message: String = "",
    val date: Timestamp? = null
)
