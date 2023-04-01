/*
 *  Created by Team Symphony on 3/31/23, 10:18 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 3/31/23, 10:12 PM
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
        val exerciseID: String? = null
)
