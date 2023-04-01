/*
 *  Created by Team Symphony on 3/31/23, 11:31 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/31/23, 11:31 PM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

data class Notification(
    val message: String = "",
    val date: Timestamp? = null
)
