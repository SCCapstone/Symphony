/*
 *  Created by Team Symphony on 2/25/23, 1:08 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/25/23, 12:43 AM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout, defined by an Exercise, Reps, and Sets
 */

data class Workout(
    val workoutName: String = "",
    val duration: Int? = null,
    val numberOfReps: Int? = null,
    val numberOfSets: Int? = null,
    val exercise: String? = null,
    val workoutID: String? = null
)
