/*
 *  Created by Team Symphony on 4/2/23, 2:50 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/2/23, 12:02 PM
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
