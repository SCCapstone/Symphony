/*
 * Created by Team Symphony 12/2/22, 1:03 AM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 1:03 AM
 */

package com.symphony.mrfit.data.model

data class Exercise(
        val name: String = "",
        val description: String = "",
        val exerciseID: String = "",
        val tags: ArrayList<String>? = null
)
