/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 3:23 PM
 */

package com.symphony.mrfit.data.model

data class Exercise(
        val name: String = "",
        val description: String = "",
        val exerciseID: String = "",
        val tags: ArrayList<String>? = null
)
