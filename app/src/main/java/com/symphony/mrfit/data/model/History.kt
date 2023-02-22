/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 7:04 PM
 */

package com.symphony.mrfit.data.model

import com.google.firebase.Timestamp

data class History(
    val name : String = "",
    val date: Timestamp? = null
)
