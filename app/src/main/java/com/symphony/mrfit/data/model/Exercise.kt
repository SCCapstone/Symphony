/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.model

data class Exercise(
        val name: String = "",
        val description: String = "",
        val tags: ArrayList<String>? = null,
        val repsFlag: Boolean = false,
        val setsFlag: Boolean = false,
        val durationFlag: Boolean = false,
        val distanceFlag: Boolean = false,
        val ownerID: String? = null,
        val exerciseID: String? = null
)
