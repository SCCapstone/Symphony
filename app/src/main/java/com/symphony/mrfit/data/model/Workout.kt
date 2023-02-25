/*
 *  Created by Team Symphony on 2/24/23, 11:21 PM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/24/23, 11:20 PM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout, defined by an Exercise, Reps, and Sets
 */

data class Workout(
    val workoutName: String = "",
    val numberOfReps: Int = 0,
    val exercise: String? = null,
    val workoutID: String? = null
)
