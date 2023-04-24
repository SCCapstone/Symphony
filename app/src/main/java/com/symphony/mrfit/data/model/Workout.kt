/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout, defined by an Exercise, Reps, and Sets
 */

data class Workout(
    val workoutName: String = "",
    val duration: String? = null,
    val distance: String? = null,
    val numberOfReps: Int? = null,
    val numberOfSets: Int? = null,
    val exercise: String? = null,
    val workoutID: String? = null
)
