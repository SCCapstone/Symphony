/*
 * Created by Team Symphony 12/2/22, 7:23 PM
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/2/22, 4:24 PM
 */

package com.symphony.mrfit.data.model

data class Workout (
    val workoutName: String = "",
    val numberOfReps: Int = 0,
    val exercise: String? = null,
    val workoutID: String? = null
)
