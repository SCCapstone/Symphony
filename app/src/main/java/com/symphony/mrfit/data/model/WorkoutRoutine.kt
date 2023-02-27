/*
 *  Created by Team Symphony on 2/25/23, 12:28 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 2/25/23, 12:06 AM
 */

package com.symphony.mrfit.data.model

/**
 * Data class for a Workout Routine, made of several smaller Workouts
 */

data class WorkoutRoutine(
    val name: String = "",
    val ownerID: String = "",
    val description: String? = null,
    val workoutList: ArrayList<String>? = null,
    val routineID: String = ""
)
