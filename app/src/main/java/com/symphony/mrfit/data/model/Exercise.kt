/*
 *  Created by Team Symphony on 2/26/23, 11:03 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/26/23, 9:30 AM
 */

package com.symphony.mrfit.data.model

data class Exercise(
        val name: String = "",
        val description: String = "",
        val tags: ArrayList<String>? = null,
        val exerciseID: String? = null
)
